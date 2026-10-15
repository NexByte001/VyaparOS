package com.vyparos.vyparos.business.dto;

import com.vyparos.vyparos.business.entity.BusinessMembership;
import com.vyparos.vyparos.business.entity.BusinessRole;

import java.time.Instant;

public class MemberSummaryDto {

    private Long membershipId;
    private Long userId;
    private String email;
    private String fullName;
    private String phone;
    private BusinessRole role;
    private String jobTitle;
    private String department;
    private boolean active;
    private Instant joinedAt;

    public MemberSummaryDto() {}

    public static MemberSummaryDto fromEntity(BusinessMembership membership) {
        MemberSummaryDto dto = new MemberSummaryDto();
        dto.setMembershipId(membership.getId());
        dto.setUserId(membership.getUser().getId());
        dto.setEmail(membership.getUser().getEmail());
        dto.setFullName(membership.getUser().getFullName());
        dto.setPhone(membership.getUser().getPhone());
        dto.setRole(membership.getRole());
        dto.setJobTitle(membership.getJobTitle());
        dto.setDepartment(membership.getDepartment());
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
