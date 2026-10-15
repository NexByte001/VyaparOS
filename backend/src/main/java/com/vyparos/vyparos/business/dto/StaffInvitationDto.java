package com.vyparos.vyparos.business.dto;

import com.vyparos.vyparos.business.entity.BusinessRole;
import com.vyparos.vyparos.business.entity.InvitationStatus;
import com.vyparos.vyparos.business.entity.StaffInvitation;

import java.time.Instant;

public class StaffInvitationDto {

    private Long id;
    private Long businessId;
    private String businessName;
    private String email;
    private String fullName;
    private String phone;
    private BusinessRole role;
    private String jobTitle;
    private String department;
    private String invitationToken;
    private InvitationStatus status;
    private String invitedByName;
    private Instant expiresAt;
    private Instant createdAt;
    private Instant acceptedAt;
    private boolean expired;
    private String inviteUrl;

    public StaffInvitationDto() {}

    public static StaffInvitationDto fromEntity(StaffInvitation invitation, String baseUrl) {
        StaffInvitationDto dto = new StaffInvitationDto();
        dto.setId(invitation.getId());
        dto.setBusinessId(invitation.getBusiness().getId());
        dto.setBusinessName(invitation.getBusiness().getName());
        dto.setEmail(invitation.getEmail());
        dto.setFullName(invitation.getFullName());
        dto.setPhone(invitation.getPhone());
        dto.setRole(invitation.getRole());
        dto.setJobTitle(invitation.getJobTitle());
        dto.setDepartment(invitation.getDepartment());
        dto.setInvitationToken(invitation.getInvitationToken());
        dto.setStatus(invitation.getStatus());
        dto.setInvitedByName(invitation.getInvitedBy().getFullName());
        dto.setExpiresAt(invitation.getExpiresAt());
        dto.setCreatedAt(invitation.getCreatedAt());
        dto.setAcceptedAt(invitation.getAcceptedAt());
        dto.setExpired(invitation.isExpired());

        if (baseUrl != null && !baseUrl.isBlank()) {
            String cleanBase = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
            dto.setInviteUrl(cleanBase + "/staff/accept-invite.html?token=" + invitation.getInvitationToken());
        } else {
            dto.setInviteUrl("/staff/accept-invite.html?token=" + invitation.getInvitationToken());
        }
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BusinessRole getRole() {
        return role;
    }

    public void setRole(BusinessRole role) {
        this.role = role;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getInvitationToken() {
        return invitationToken;
    }

    public void setInvitationToken(String invitationToken) {
        this.invitationToken = invitationToken;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    public String getInvitedByName() {
        return invitedByName;
    }

    public void setInvitedByName(String invitedByName) {
        this.invitedByName = invitedByName;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(Instant acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public String getInviteUrl() {
        return inviteUrl;
    }

    public void setInviteUrl(String inviteUrl) {
        this.inviteUrl = inviteUrl;
    }
}
