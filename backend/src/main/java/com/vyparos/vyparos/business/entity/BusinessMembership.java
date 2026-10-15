package com.vyparos.vyparos.business.entity;

import com.vyparos.vyparos.identity.entity.User;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "business_memberships",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "business_id"})
    }
)
public class BusinessMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BusinessRole role;

    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private Instant joinedAt = Instant.now();

    public BusinessMembership() {}

    public BusinessMembership(User user, Business business, BusinessRole role) {
        this.user = user;
        this.business = business;
        this.role = role;
        this.active = true;
        this.joinedAt = Instant.now();
    }

    public BusinessMembership(User user, Business business, BusinessRole role, String jobTitle, String department) {
        this.user = user;
        this.business = business;
        this.role = role;
        this.jobTitle = jobTitle;
        this.department = department;
        this.active = true;
        this.joinedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
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
