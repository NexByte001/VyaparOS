package com.vyparos.vyparos.attendance.dto;

import com.vyparos.vyparos.attendance.entity.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;

public class MarkAttendanceRequest {

    @NotNull(message = "Staff member ID is mandatory")
    private Long staffMembershipId;

    private LocalDate date;

    @NotNull(message = "Attendance status is mandatory")
    private AttendanceStatus status;

    private Instant clockInTime;
    private Instant clockOutTime;
    private Integer durationMinutes;
    private Integer customersAttended;
    private String notes;

    public MarkAttendanceRequest() {}

    public Long getStaffMembershipId() {
        return staffMembershipId;
    }

    public void setStaffMembershipId(Long staffMembershipId) {
        this.staffMembershipId = staffMembershipId;
    }

    public LocalDate getDate() {
        return date != null ? date : LocalDate.now();
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
        return customersAttended;
    }

    public void setCustomersAttended(Integer customersAttended) {
        this.customersAttended = customersAttended;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
