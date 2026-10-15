package com.vyparos.vyparos.business.service.impl;

import com.vyparos.vyparos.business.dto.*;
import com.vyparos.vyparos.business.entity.*;
import com.vyparos.vyparos.business.repository.BusinessMembershipRepository;
import com.vyparos.vyparos.business.repository.BusinessRepository;
import com.vyparos.vyparos.business.repository.BusinessSettingsRepository;
import com.vyparos.vyparos.business.repository.StaffInvitationRepository;
import com.vyparos.vyparos.business.service.BusinessService;
import com.vyparos.vyparos.exception.BadRequestException;
import com.vyparos.vyparos.exception.ConflictException;
import com.vyparos.vyparos.exception.ForbiddenException;
import com.vyparos.vyparos.exception.ResourceNotFoundException;
import com.vyparos.vyparos.identity.entity.User;
import com.vyparos.vyparos.identity.repository.UserRepository;
import com.vyparos.vyparos.security.TenantSecurityGuard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessMembershipRepository membershipRepository;
    private final BusinessSettingsRepository settingsRepository;
    private final StaffInvitationRepository staffInvitationRepository;
    private final UserRepository userRepository;
    private final TenantSecurityGuard securityGuard;
    private final SecureRandom secureRandom = new SecureRandom();

    public BusinessServiceImpl(
            BusinessRepository businessRepository,
            BusinessMembershipRepository membershipRepository,
            BusinessSettingsRepository settingsRepository,
            StaffInvitationRepository staffInvitationRepository,
            UserRepository userRepository,
            TenantSecurityGuard securityGuard) {
        this.businessRepository = businessRepository;
        this.membershipRepository = membershipRepository;
        this.settingsRepository = settingsRepository;
        this.staffInvitationRepository = staffInvitationRepository;
        this.userRepository = userRepository;
        this.securityGuard = securityGuard;
    }

    @Override
    @Transactional
    public BusinessDetailDto createBusiness(Long userId, CreateBusinessRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String currencyCode = "INR";
        if (request.getCurrency() != null && !request.getCurrency().isBlank()) {
            String curr = request.getCurrency().trim().toUpperCase();
            try {
                java.util.Currency.getInstance(curr);
                currencyCode = curr;
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid ISO-4217 currency code: " + request.getCurrency() + ". Must be a 3-letter currency code such as INR, USD, EUR.");
            }
        }

        Business business = new Business(
                request.getName().trim(),
                request.getBusinessType().trim(),
                currencyCode,
                request.getTimezone(),
                request.getAddress(),
                request.getPhone(),
                request.getEmail()
        );
        Business savedBusiness = businessRepository.save(business);

        // Provision creator as OWNER
        BusinessMembership ownerMembership = new BusinessMembership(user, savedBusiness, BusinessRole.OWNER);
        membershipRepository.save(ownerMembership);

        // Initialize default business settings
        BusinessSettings settings = new BusinessSettings(savedBusiness);
        settingsRepository.save(settings);

        return BusinessDetailDto.fromEntity(savedBusiness, BusinessRole.OWNER);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BusinessMembershipDto> getUserBusinesses(Long userId) {
        return membershipRepository.findAllByUserIdAndActiveTrue(userId).stream()
                .map(BusinessMembershipDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BusinessDetailDto getBusinessDetails(Long businessId) {
        BusinessMembership membership = securityGuard.requireMembership(businessId);
        return BusinessDetailDto.fromEntity(membership.getBusiness(), membership.getRole());
    }

    @Override
    @Transactional
    public BusinessDetailDto updateBusiness(Long businessId, UpdateBusinessRequest request) {
        BusinessMembership membership = securityGuard.requireAdminOrOwner(businessId);
        Business business = membership.getBusiness();

        if (request.getName() != null && !request.getName().isBlank()) business.setName(request.getName().trim());
        if (request.getBusinessType() != null && !request.getBusinessType().isBlank()) business.setBusinessType(request.getBusinessType().trim());
        if (request.getCurrency() != null && !request.getCurrency().isBlank()) {
            String curr = request.getCurrency().trim().toUpperCase();
            try {
                java.util.Currency.getInstance(curr);
                business.setCurrency(curr);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid ISO-4217 currency code: " + request.getCurrency() + ". Must be a 3-letter currency code such as INR, USD, EUR.");
            }
        }
        if (request.getTimezone() != null && !request.getTimezone().isBlank()) business.setTimezone(request.getTimezone().trim());
        if (request.getAddress() != null) business.setAddress(request.getAddress().trim());
        if (request.getPhone() != null) business.setPhone(request.getPhone().trim());
        if (request.getEmail() != null) business.setEmail(request.getEmail().trim());

        Business updated = businessRepository.save(business);
        return BusinessDetailDto.fromEntity(updated, membership.getRole());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberSummaryDto> getMembers(Long businessId) {
        securityGuard.requireMembership(businessId);
        return membershipRepository.findAllByBusinessId(businessId).stream()
                .map(MemberSummaryDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public AddMemberResponse addMember(Long businessId, AddMemberRequest request) {
        BusinessMembership adminMembership = securityGuard.requireAdminOrOwner(businessId);

        // Staff cannot invite OWNER, only OWNER can assign OWNER
        if (request.getRole() == BusinessRole.OWNER && adminMembership.getRole() != BusinessRole.OWNER) {
            throw new ForbiddenException("Only the business Owner can assign another Owner");
        }

        String email = request.getEmail().trim().toLowerCase();
        Optional<User> existingUserOpt = userRepository.findByEmailIgnoreCase(email);

        if (existingUserOpt.isPresent()) {
            User targetUser = existingUserOpt.get();
            Optional<BusinessMembership> existingMembershipOpt =
                    membershipRepository.findByBusinessIdAndUserId(businessId, targetUser.getId());

            if (existingMembershipOpt.isPresent()) {
                BusinessMembership existingMembership = existingMembershipOpt.get();
                if (existingMembership.isActive()) {
                    throw new ConflictException("User is already an active member of this business workspace");
                } else {
                    // Reactivate member
                    existingMembership.setActive(true);
                    existingMembership.setRole(request.getRole());
                    if (request.getJobTitle() != null && !request.getJobTitle().isBlank()) {
                        existingMembership.setJobTitle(request.getJobTitle().trim());
                    }
                    if (request.getDepartment() != null && !request.getDepartment().isBlank()) {
                        existingMembership.setDepartment(request.getDepartment().trim());
                    }
                    BusinessMembership saved = membershipRepository.save(existingMembership);
                    return AddMemberResponse.added(
                            MemberSummaryDto.fromEntity(saved),
                            "Member reactivated and restored to workspace team"
                    );
                }
            }

            // User is registered in system but not a member of this workspace -> add directly
            BusinessMembership newMembership = new BusinessMembership(
                    targetUser,
                    adminMembership.getBusiness(),
                    request.getRole(),
                    request.getJobTitle() != null ? request.getJobTitle().trim() : null,
                    request.getDepartment() != null ? request.getDepartment().trim() : null
            );
            BusinessMembership saved = membershipRepository.save(newMembership);
            return AddMemberResponse.added(
                    MemberSummaryDto.fromEntity(saved),
                    "Existing user added directly to workspace"
            );
        }

        // New / Unregistered user workflow: Create pending Staff Invitation
        Optional<StaffInvitation> pendingInviteOpt =
                staffInvitationRepository.findByBusinessIdAndEmailIgnoreCaseAndStatus(businessId, email, InvitationStatus.PENDING);

        if (pendingInviteOpt.isPresent()) {
            StaffInvitation pendingInvite = pendingInviteOpt.get();
            if (!pendingInvite.isExpired()) {
                throw new ConflictException("An active invitation is already pending for " + email + ". You can resend or manage it in the Pending Invitations tab.");
            } else {
                pendingInvite.setStatus(InvitationStatus.EXPIRED);
                staffInvitationRepository.save(pendingInvite);
            }
        }

        String fullName = (request.getFullName() != null && !request.getFullName().isBlank())
                ? request.getFullName().trim()
                : email.substring(0, email.indexOf('@'));

        String token = UUID.randomUUID().toString().replace("-", "") + Long.toHexString(secureRandom.nextLong());
        Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);

        StaffInvitation invitation = new StaffInvitation(
                adminMembership.getBusiness(),
                email,
                fullName,
                request.getPhone() != null ? request.getPhone().trim() : null,
                request.getRole(),
                request.getJobTitle() != null ? request.getJobTitle().trim() : null,
                request.getDepartment() != null ? request.getDepartment().trim() : null,
                token,
                adminMembership.getUser(),
                expiresAt
        );

        StaffInvitation savedInvitation = staffInvitationRepository.save(invitation);
        StaffInvitationDto dto = StaffInvitationDto.fromEntity(savedInvitation, "");

        return AddMemberResponse.invited(
                dto,
                "Staff invitation created successfully. Share the invite link with the new employee to join."
        );
    }

    @Override
    @Transactional
    public MemberSummaryDto updateMember(Long businessId, Long memberId, UpdateMemberRequest request) {
        BusinessMembership adminMembership = securityGuard.requireAdminOrOwner(businessId);

        BusinessMembership targetMembership = membershipRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member record not found"));

        if (!targetMembership.getBusiness().getId().equals(businessId)) {
            throw new ResourceNotFoundException("Member record not found in this business");
        }

        // Prevent non-owners from modifying owner memberships
        if (targetMembership.getRole() == BusinessRole.OWNER && adminMembership.getRole() != BusinessRole.OWNER) {
            throw new ForbiddenException("Cannot modify Owner membership");
        }

        if (request.getRole() != null) {
            if (request.getRole() == BusinessRole.OWNER && adminMembership.getRole() != BusinessRole.OWNER) {
                throw new ForbiddenException("Only an Owner can promote a member to Owner");
            }
            targetMembership.setRole(request.getRole());
        }

        if (request.getActive() != null) {
            // Prevent deactivating the last active owner
            if (!request.getActive() && targetMembership.getRole() == BusinessRole.OWNER) {
                long activeOwners = membershipRepository.countByBusinessIdAndRoleAndActiveTrue(businessId, BusinessRole.OWNER);
                if (activeOwners <= 1) {
                    throw new BadRequestException("Cannot deactivate the sole Owner of this business workspace");
                }
            }
            targetMembership.setActive(request.getActive());
        }

        if (request.getJobTitle() != null) {
            targetMembership.setJobTitle(request.getJobTitle().trim());
        }
        if (request.getDepartment() != null) {
            targetMembership.setDepartment(request.getDepartment().trim());
        }

        BusinessMembership saved = membershipRepository.save(targetMembership);
        return MemberSummaryDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public MemberSummaryDto removeMember(Long businessId, Long memberId) {
        BusinessMembership adminMembership = securityGuard.requireAdminOrOwner(businessId);

        BusinessMembership targetMembership = membershipRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member record not found"));

        if (!targetMembership.getBusiness().getId().equals(businessId)) {
            throw new ResourceNotFoundException("Member record not found in this business workspace");
        }

        if (targetMembership.getRole() == BusinessRole.OWNER) {
            if (adminMembership.getRole() != BusinessRole.OWNER) {
                throw new ForbiddenException("Only an Owner can remove another Owner");
            }
            long activeOwners = membershipRepository.countByBusinessIdAndRoleAndActiveTrue(businessId, BusinessRole.OWNER);
            if (activeOwners <= 1) {
                throw new BadRequestException("Cannot remove the sole Owner of this business workspace");
            }
        }

        // Soft deactivation to preserve historical records (invoices, audit logs, appointments)
        targetMembership.setActive(false);
        BusinessMembership saved = membershipRepository.save(targetMembership);
        return MemberSummaryDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public List<StaffInvitationDto> getInvitations(Long businessId) {
        securityGuard.requireMembership(businessId);
        List<StaffInvitation> invitations = staffInvitationRepository.findAllByBusinessIdOrderByCreatedAtDesc(businessId);

        // Auto-expire pending invitations past expiresAt
        for (StaffInvitation inv : invitations) {
            if (inv.getStatus() == InvitationStatus.PENDING && inv.isExpired()) {
                inv.setStatus(InvitationStatus.EXPIRED);
                staffInvitationRepository.save(inv);
            }
        }

        return invitations.stream()
                .map(inv -> StaffInvitationDto.fromEntity(inv, ""))
                .toList();
    }

    @Override
    @Transactional
    public StaffInvitationDto resendInvitation(Long businessId, Long invitationId) {
        securityGuard.requireAdminOrOwner(businessId);

        StaffInvitation invitation = staffInvitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        if (!invitation.getBusiness().getId().equals(businessId)) {
            throw new ResourceNotFoundException("Invitation not found in this business workspace");
        }

        if (invitation.getStatus() == InvitationStatus.ACCEPTED) {
            throw new ConflictException("Cannot resend an already accepted invitation");
        }

        // Generate a new secure token and extend expiration by 7 days
        String newToken = UUID.randomUUID().toString().replace("-", "") + Long.toHexString(secureRandom.nextLong());
        invitation.setInvitationToken(newToken);
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));

        StaffInvitation saved = staffInvitationRepository.save(invitation);
        return StaffInvitationDto.fromEntity(saved, "");
    }

    @Override
    @Transactional
    public StaffInvitationDto revokeInvitation(Long businessId, Long invitationId) {
        securityGuard.requireAdminOrOwner(businessId);

        StaffInvitation invitation = staffInvitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        if (!invitation.getBusiness().getId().equals(businessId)) {
            throw new ResourceNotFoundException("Invitation not found in this business workspace");
        }

        if (invitation.getStatus() == InvitationStatus.ACCEPTED) {
            throw new ConflictException("Cannot revoke an already accepted invitation");
        }

        invitation.setStatus(InvitationStatus.REVOKED);
        StaffInvitation saved = staffInvitationRepository.save(invitation);
        return StaffInvitationDto.fromEntity(saved, "");
    }

    @Override
    @Transactional(readOnly = true)
    public BusinessSettingsDto getSettings(Long businessId) {
        securityGuard.requireMembership(businessId);
        BusinessSettings settings = settingsRepository.findByBusinessId(businessId)
                .orElseGet(() -> {
                    Business business = businessRepository.findById(businessId)
                            .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
                    return settingsRepository.save(new BusinessSettings(business));
                });
        return BusinessSettingsDto.fromEntity(settings);
    }

    @Override
    @Transactional
    public BusinessSettingsDto updateSettings(Long businessId, BusinessSettingsDto request) {
        securityGuard.requireAdminOrOwner(businessId);
        BusinessSettings settings = settingsRepository.findByBusinessId(businessId)
                .orElseGet(() -> {
                    Business business = businessRepository.findById(businessId)
                            .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
                    return new BusinessSettings(business);
                });

        if (request.getTaxRate() != null) settings.setTaxRate(request.getTaxRate());
        if (request.getTaxName() != null) settings.setTaxName(request.getTaxName().trim());
        if (request.getInvoicePrefix() != null) settings.setInvoicePrefix(request.getInvoicePrefix().trim());
        if (request.getDefaultAppointmentDuration() != null) settings.setDefaultAppointmentDuration(request.getDefaultAppointmentDuration());
        if (request.getOpeningTime() != null) settings.setOpeningTime(request.getOpeningTime());
        if (request.getClosingTime() != null) settings.setClosingTime(request.getClosingTime());
        if (request.getSettingsJson() != null) settings.setSettingsJson(request.getSettingsJson());

        BusinessSettings updated = settingsRepository.save(settings);
        return BusinessSettingsDto.fromEntity(updated);
    }
}
