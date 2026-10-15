package com.vyparos.vyparos.business.service.impl;

import com.vyparos.vyparos.business.dto.BusinessMembershipDto;
import com.vyparos.vyparos.business.dto.MemberSummaryDto;
import com.vyparos.vyparos.business.dto.RegisterAndAcceptInviteRequest;
import com.vyparos.vyparos.business.dto.StaffInvitationDto;
import com.vyparos.vyparos.business.entity.BusinessMembership;
import com.vyparos.vyparos.business.entity.InvitationStatus;
import com.vyparos.vyparos.business.entity.StaffInvitation;
import com.vyparos.vyparos.business.repository.BusinessMembershipRepository;
import com.vyparos.vyparos.business.repository.StaffInvitationRepository;
import com.vyparos.vyparos.business.service.InvitationService;
import com.vyparos.vyparos.exception.BadRequestException;
import com.vyparos.vyparos.exception.ConflictException;
import com.vyparos.vyparos.exception.ForbiddenException;
import com.vyparos.vyparos.exception.ResourceNotFoundException;
import com.vyparos.vyparos.identity.dto.AuthResponse;
import com.vyparos.vyparos.identity.dto.UserSummaryDto;
import com.vyparos.vyparos.identity.entity.User;
import com.vyparos.vyparos.identity.repository.UserRepository;
import com.vyparos.vyparos.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class InvitationServiceImpl implements InvitationService {

    private final StaffInvitationRepository staffInvitationRepository;
    private final BusinessMembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public InvitationServiceImpl(
            StaffInvitationRepository staffInvitationRepository,
            BusinessMembershipRepository membershipRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider tokenProvider) {
        this.staffInvitationRepository = staffInvitationRepository;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    @Transactional
    public StaffInvitationDto getInvitation(String token) {
        StaffInvitation invitation = staffInvitationRepository.findByInvitationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation link is invalid or does not exist"));

        if (invitation.getStatus() == InvitationStatus.PENDING && invitation.isExpired()) {
            invitation.setStatus(InvitationStatus.EXPIRED);
            staffInvitationRepository.save(invitation);
        }

        if (invitation.getStatus() == InvitationStatus.EXPIRED) {
            throw new BadRequestException("This invitation has expired. Please ask the business owner to resend it.");
        }

        if (invitation.getStatus() == InvitationStatus.REVOKED) {
            throw new BadRequestException("This invitation has been revoked by the workspace administrator.");
        }

        if (invitation.getStatus() == InvitationStatus.ACCEPTED) {
            throw new ConflictException("This invitation has already been accepted.");
        }

        return StaffInvitationDto.fromEntity(invitation, "");
    }

    @Override
    @Transactional
    public AuthResponse registerAndAccept(String token, RegisterAndAcceptInviteRequest request) {
        StaffInvitation invitation = staffInvitationRepository.findByInvitationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation link is invalid or does not exist"));

        if (invitation.getStatus() == InvitationStatus.PENDING && invitation.isExpired()) {
            invitation.setStatus(InvitationStatus.EXPIRED);
            staffInvitationRepository.save(invitation);
        }

        if (invitation.getStatus() == InvitationStatus.EXPIRED) {
            throw new BadRequestException("This invitation has expired. Please ask the business owner to resend it.");
        }

        if (invitation.getStatus() == InvitationStatus.REVOKED) {
            throw new BadRequestException("This invitation has been revoked by the workspace administrator.");
        }

        if (invitation.getStatus() == InvitationStatus.ACCEPTED) {
            throw new ConflictException("This invitation has already been accepted.");
        }

        String email = invitation.getEmail().toLowerCase().trim();
        Optional<User> existingUserOpt = userRepository.findByEmailIgnoreCase(email);

        User user;
        if (existingUserOpt.isPresent()) {
            user = existingUserOpt.get();
        } else {
            String fullName = (request.getFullName() != null && !request.getFullName().isBlank())
                    ? request.getFullName().trim()
                    : invitation.getFullName();

            String phone = (request.getPhone() != null && !request.getPhone().isBlank())
                    ? request.getPhone().trim()
                    : invitation.getPhone();

            User newUser = new User(email, passwordEncoder.encode(request.getPassword()), fullName, phone);
            user = userRepository.save(newUser);
        }

        // Link user to workspace as BusinessMembership
        Optional<BusinessMembership> existingMembershipOpt =
                membershipRepository.findByBusinessIdAndUserId(invitation.getBusiness().getId(), user.getId());

        BusinessMembership membership;
        if (existingMembershipOpt.isPresent()) {
            membership = existingMembershipOpt.get();
            membership.setActive(true);
            membership.setRole(invitation.getRole());
            if (invitation.getJobTitle() != null) membership.setJobTitle(invitation.getJobTitle());
            if (invitation.getDepartment() != null) membership.setDepartment(invitation.getDepartment());
        } else {
            membership = new BusinessMembership(
                    user,
                    invitation.getBusiness(),
                    invitation.getRole(),
                    invitation.getJobTitle(),
                    invitation.getDepartment()
            );
        }
        membershipRepository.save(membership);

        // Mark invitation ACCEPTED
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setAcceptedAt(Instant.now());
        staffInvitationRepository.save(invitation);

        // Generate JWT authentication token
        String jwt = tokenProvider.generateToken(user.getId(), user.getEmail());
        UserSummaryDto userDto = UserSummaryDto.fromEntity(user);

        List<BusinessMembership> memberships = membershipRepository.findAllByUserIdAndActiveTrue(user.getId());
        List<BusinessMembershipDto> membershipDtos = memberships.stream()
                .map(BusinessMembershipDto::fromEntity)
                .toList();

        return new AuthResponse(jwt, tokenProvider.getExpirationMs(), userDto, membershipDtos);
    }

    @Override
    @Transactional
    public MemberSummaryDto acceptAuthenticated(String token, Long userId) {
        StaffInvitation invitation = staffInvitationRepository.findByInvitationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation link is invalid or does not exist"));

        if (invitation.getStatus() == InvitationStatus.PENDING && invitation.isExpired()) {
            invitation.setStatus(InvitationStatus.EXPIRED);
            staffInvitationRepository.save(invitation);
        }

        if (invitation.getStatus() == InvitationStatus.EXPIRED) {
            throw new BadRequestException("This invitation has expired. Please ask the business owner to resend it.");
        }

        if (invitation.getStatus() == InvitationStatus.REVOKED) {
            throw new BadRequestException("This invitation has been revoked by the workspace administrator.");
        }

        if (invitation.getStatus() == InvitationStatus.ACCEPTED) {
            throw new ConflictException("This invitation has already been accepted.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Security check: invitation email must match current user email
        if (!invitation.getEmail().equalsIgnoreCase(user.getEmail())) {
            throw new ForbiddenException("This invitation was issued for " + invitation.getEmail() + ", but you are logged in as " + user.getEmail());
        }

        Optional<BusinessMembership> existingMembershipOpt =
                membershipRepository.findByBusinessIdAndUserId(invitation.getBusiness().getId(), user.getId());

        BusinessMembership membership;
        if (existingMembershipOpt.isPresent()) {
            membership = existingMembershipOpt.get();
            membership.setActive(true);
            membership.setRole(invitation.getRole());
            if (invitation.getJobTitle() != null) membership.setJobTitle(invitation.getJobTitle());
            if (invitation.getDepartment() != null) membership.setDepartment(invitation.getDepartment());
        } else {
            membership = new BusinessMembership(
                    user,
                    invitation.getBusiness(),
                    invitation.getRole(),
                    invitation.getJobTitle(),
                    invitation.getDepartment()
            );
        }
        BusinessMembership savedMembership = membershipRepository.save(membership);

        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setAcceptedAt(Instant.now());
        staffInvitationRepository.save(invitation);

        return MemberSummaryDto.fromEntity(savedMembership);
    }
}
