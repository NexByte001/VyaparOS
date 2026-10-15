package com.vyparos.vyparos.business.dto;

import com.vyparos.vyparos.business.entity.BusinessMembership;
import com.vyparos.vyparos.business.entity.BusinessRole;

import java.time.Instant;

public class BusinessMembershipDto {

    private Long membershipId;
    private Long businessId;
    private String businessName;
    private String businessType;
    private String currency;
    private String timezone;
    private BusinessRole role;
    private boolean active;
    private Instant joinedAt;

    public BusinessMembershipDto() {}

    public static BusinessMembershipDto fromEntity(BusinessMembership membership) {
        BusinessMembershipDto dto = new BusinessMembershipDto();
        dto.setMembershipId(membership.getId());
        dto.setBusinessId(membership.getBusiness().getId());
        dto.setBusinessName(membership.getBusiness().getName());
        dto.setBusinessType(membership.getBusiness().getBusinessType());
        dto.setCurrency(membership.getBusiness().getCurrency());
        dto.setTimezone(membership.getBusiness().getTimezone());
        dto.setRole(membership.getRole());
        dto.setActive(membership.isActive());
        dto.setJoinedAt(membership.getJoinedAt());
        return dto;
    }

    public Long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public BusinessRole getRole() {
        return role;
    }

    public void setRole(BusinessRole role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }
}
