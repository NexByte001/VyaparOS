package com.vyparos.vyparos.attendance.dto;

import com.vyparos.vyparos.attendance.entity.AttendanceStatus;

import java.time.Instant;
import java.time.LocalDate;

public class DailyStaffAttendanceDto {

    private Long staffMembershipId;
    private String staffName;
    private String email;
    private String role;
    private Long attendanceRecordId;
    private LocalDate date;
    private AttendanceStatus status;
    private Instant clockInTime;
    private Instant clockOutTime;
    private Integer durationMinutes;
    private Integer customersAttended;
    private Boolean isManualCustomersCount;
    private String notes;

    public DailyStaffAttendanceDto() {}

    public Long getStaffMembershipId() {
        return staffMembershipId;
    }

    public void setStaffMembershipId(Long staffMembershipId) {
        this.staffMembershipId = staffMembershipId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getAttendanceRecordId() {
        return attendanceRecordId;
    }

    public void setAttendanceRecordId(Long attendanceRecordId) {
        this.attendanceRecordId = attendanceRecordId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
