package com.vyparos.vyparos.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class AttendanceCorrectionRequest {

    @NotNull(message = "New clock-in time is required")
    private Instant newClockInTime;

    private Instant newClockOutTime;

    @NotBlank(message = "Mandatory reason for administrative attendance correction is required")
    private String reason;

    public AttendanceCorrectionRequest() {}

    public AttendanceCorrectionRequest(Instant newClockInTime, Instant newClockOutTime, String reason) {
        this.newClockInTime = newClockInTime;
        this.newClockOutTime = newClockOutTime;
        this.reason = reason;
    }

    public Instant getNewClockInTime() {
        return newClockInTime;
    }

    public void setNewClockInTime(Instant newClockInTime) {
        this.newClockInTime = newClockInTime;
    }

    public Instant getNewClockOutTime() {
        return newClockOutTime;
    }

    public void setNewClockOutTime(Instant newClockOutTime) {
        this.newClockOutTime = newClockOutTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
