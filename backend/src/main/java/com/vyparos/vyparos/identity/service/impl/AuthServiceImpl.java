package com.vyparos.vyparos.identity.service.impl;

import com.vyparos.vyparos.business.dto.BusinessMembershipDto;
import com.vyparos.vyparos.business.entity.BusinessMembership;
import com.vyparos.vyparos.business.repository.BusinessMembershipRepository;
import com.vyparos.vyparos.exception.ConflictException;
import com.vyparos.vyparos.exception.ResourceNotFoundException;
import com.vyparos.vyparos.exception.UnauthorizedException;
import com.vyparos.vyparos.identity.dto.AuthResponse;
import com.vyparos.vyparos.identity.dto.LoginRequest;
import com.vyparos.vyparos.identity.dto.RegisterRequest;
import com.vyparos.vyparos.identity.dto.UserSummaryDto;
import com.vyparos.vyparos.identity.entity.User;
import com.vyparos.vyparos.identity.repository.UserRepository;
import com.vyparos.vyparos.identity.service.AuthService;
import com.vyparos.vyparos.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BusinessMembershipRepository membershipRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(
            UserRepository userRepository,
            BusinessMembershipRepository membershipRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.membershipRepository = membershipRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("An account with email " + email + " already exists");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(email, encodedPassword, request.getFullName().trim(), request.getPhone() != null ? request.getPhone().trim() : null);
        User savedUser = userRepository.save(user);

        String token = tokenProvider.generateToken(savedUser.getId(), savedUser.getEmail());
        UserSummaryDto userDto = UserSummaryDto.fromEntity(savedUser);

        return new AuthResponse(token, tokenProvider.getExpirationMs(), userDto, List.of());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        if (!user.isActive()) {
            throw new UnauthorizedException("Account is disabled. Please contact support.");
        }

        String token = tokenProvider.generateToken(user.getId(), user.getEmail());
        UserSummaryDto userDto = UserSummaryDto.fromEntity(user);

        List<BusinessMembership> memberships = membershipRepository.findAllByUserIdAndActiveTrue(user.getId());
        List<BusinessMembershipDto> membershipDtos = memberships.stream()
                .map(BusinessMembershipDto::fromEntity)
                .toList();

        return new AuthResponse(token, tokenProvider.getExpirationMs(), userDto, membershipDtos);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = tokenProvider.generateToken(user.getId(), user.getEmail());
        UserSummaryDto userDto = UserSummaryDto.fromEntity(user);

        List<BusinessMembership> memberships = membershipRepository.findAllByUserIdAndActiveTrue(user.getId());
        List<BusinessMembershipDto> membershipDtos = memberships.stream()
                .map(BusinessMembershipDto::fromEntity)
                .toList();

        return new AuthResponse(token, tokenProvider.getExpirationMs(), userDto, membershipDtos);
    }
}
