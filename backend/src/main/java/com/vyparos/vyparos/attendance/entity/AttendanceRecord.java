package com.vyparos.vyparos.attendance.entity;

import com.vyparos.vyparos.business.entity.Business;
import com.vyparos.vyparos.business.entity.BusinessMembership;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "attendance_records",
    indexes = {
        @Index(name = "idx_att_member_time", columnList = "staff_membership_id, clock_in_time"),
        @Index(name = "idx_att_biz_date", columnList = "business_id, attendance_date")
    }
)
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "staff_membership_id", nullable = false)
    private BusinessMembership staffMembership;

    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    @Column(name = "clock_in_time", nullable = false)
    private Instant clockInTime;

    @Column(name = "clock_out_time")
    private Instant clockOutTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AttendanceStatus status = AttendanceStatus.OPEN;

    @Column(name = "customers_attended")
    private Integer customersAttended = 0;

    @Column(name = "is_manual_customers_count")
    private Boolean isManualCustomersCount = false;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "attendanceRecord", cascade = CascadeType.ALL)
    @OrderBy("createdAt DESC")
    private List<AttendanceCorrection> corrections = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public AttendanceRecord() {}

    public AttendanceRecord(Business business, BusinessMembership staffMembership, Instant clockInTime, String notes) {
        this.business = business;
        this.staffMembership = staffMembership;
        this.clockInTime = clockInTime != null ? clockInTime : Instant.now();
        this.attendanceDate = this.clockInTime.atZone(ZoneId.systemDefault()).toLocalDate();
        this.notes = notes;
        this.status = AttendanceStatus.OPEN;
        this.customersAttended = 0;
        this.isManualCustomersCount = false;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public AttendanceRecord(Business business, BusinessMembership staffMembership, LocalDate attendanceDate, AttendanceStatus status, Instant clockInTime, Instant clockOutTime, Integer durationMinutes, Integer customersAttended, String notes) {
        this.business = business;
        this.staffMembership = staffMembership;
        this.attendanceDate = attendanceDate != null ? attendanceDate : LocalDate.now();
        this.clockInTime = clockInTime != null ? clockInTime : this.attendanceDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        this.clockOutTime = clockOutTime;
        this.durationMinutes = durationMinutes;
        this.status = status != null ? status : AttendanceStatus.PRESENT;
        this.customersAttended = customersAttended != null ? customersAttended : 0;
        this.isManualCustomersCount = customersAttended != null;
        this.notes = notes;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.attendanceDate == null) {
            this.attendanceDate = this.clockInTime != null
                    ? this.clockInTime.atZone(ZoneId.systemDefault()).toLocalDate()
                    : LocalDate.now();
        }
        if (this.clockInTime == null) {
            this.clockInTime = this.attendanceDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
        if (this.customersAttended == null) {
            this.customersAttended = 0;
        }
        if (this.isManualCustomersCount == null) {
            this.isManualCustomersCount = false;
        }
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = Instant.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (this.attendanceDate == null && this.clockInTime != null) {
            this.attendanceDate = this.clockInTime.atZone(ZoneId.systemDefault()).toLocalDate();
        }
        this.updatedAt = Instant.now();
    }

    public void addCorrection(AttendanceCorrection correction) {
        corrections.add(correction);
        correction.setAttendanceRecord(this);
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

    public BusinessMembership getStaffMembership() {
        return staffMembership;
    }

    public void setStaffMembership(BusinessMembership staffMembership) {
        this.staffMembership = staffMembership;
    }

    public LocalDate getAttendanceDate() {
        if (attendanceDate != null) {
            return attendanceDate;
        }
        if (clockInTime != null) {
            return clockInTime.atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return LocalDate.now();
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Integer getCustomersAttended() {
        return customersAttended != null ? customersAttended : 0;
    }

    public void setCustomersAttended(Integer customersAttended) {
        this.customersAttended = customersAttended;
    }

    public Boolean getIsManualCustomersCount() {
        return isManualCustomersCount != null ? isManualCustomersCount : false;
    }

    public void setIsManualCustomersCount(Boolean isManualCustomersCount) {
        this.isManualCustomersCount = isManualCustomersCount;
    }

    public Instant getClockInTime() {
        return clockInTime;
    }

    public void setClockInTime(Instant clockInTime) {
        this.clockInTime = clockInTime;
    }

    public Instant getClockOutTime() {
        return clockOutTime;
    }

    public void setClockOutTime(Instant clockOutTime) {
        this.clockOutTime = clockOutTime;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<AttendanceCorrection> getCorrections() {
        return corrections;
    }

    public void setCorrections(List<AttendanceCorrection> corrections) {
        this.corrections = corrections;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
