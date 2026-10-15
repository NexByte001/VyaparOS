package com.vyparos.vyparos.business.entity;

import com.vyparos.vyparos.identity.entity.User;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "staff_invitations",
    indexes = {
        @Index(name = "idx_invitation_business_status", columnList = "business_id, status"),
        @Index(name = "idx_invitation_email", columnList = "email"),
        @Index(name = "idx_invitation_token", columnList = "invitation_token")
    }
)
public class StaffInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(length = 30)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BusinessRole role;

    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @Column(length = 100)
    private String department;

    @Column(name = "invitation_token", nullable = false, unique = true, length = 120)
    private String invitationToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InvitationStatus status = InvitationStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invited_by_user_id", nullable = false)
    private User invitedBy;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "accepted_at")
    private Instant acceptedAt;

    public StaffInvitation() {}

    public StaffInvitation(
            Business business,
            String email,
            String fullName,
            String phone,
            BusinessRole role,
            String jobTitle,
            String department,
            String invitationToken,
            User invitedBy,
            Instant expiresAt) {
        this.business = business;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
        this.jobTitle = jobTitle;
        this.department = department;
        this.invitationToken = invitationToken;
        this.invitedBy = invitedBy;
        this.expiresAt = expiresAt;
        this.status = InvitationStatus.PENDING;
        this.createdAt = Instant.now();
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(Instant.now());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
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

    public User getInvitedBy() {
        return invitedBy;
    }

    public void setInvitedBy(User invitedBy) {
        this.invitedBy = invitedBy;
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
}
