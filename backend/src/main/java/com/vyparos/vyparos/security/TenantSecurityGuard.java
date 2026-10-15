package com.vyparos.vyparos.security;

import com.vyparos.vyparos.business.entity.BusinessMembership;
import com.vyparos.vyparos.business.entity.BusinessRole;
import com.vyparos.vyparos.business.repository.BusinessMembershipRepository;
import com.vyparos.vyparos.exception.ForbiddenException;
import com.vyparos.vyparos.exception.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class TenantSecurityGuard {

    private final BusinessMembershipRepository membershipRepository;

    public TenantSecurityGuard(BusinessMembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
            throw new ForbiddenException("No authenticated user in context");
        }
        return principal.getId();
    }

    public BusinessMembership requireMembership(Long businessId) {
        Long userId = getCurrentUserId();
        return membershipRepository.findByBusinessIdAndUserIdAndActiveTrue(businessId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Business workspace not found or access denied"));
    }

    public BusinessMembership requireRole(Long businessId, BusinessRole... allowedRoles) {
        BusinessMembership membership = requireMembership(businessId);
        List<BusinessRole> allowedList = Arrays.asList(allowedRoles);

        if (!allowedList.contains(membership.getRole())) {
            throw new ForbiddenException("You do not have permission to perform this action in this business workspace");
        }
        return membership;
    }

    public BusinessMembership requireAdminOrOwner(Long businessId) {
        return requireRole(businessId, BusinessRole.OWNER, BusinessRole.ADMIN);
    }
}
