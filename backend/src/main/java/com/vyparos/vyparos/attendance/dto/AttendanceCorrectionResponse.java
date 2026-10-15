package com.vyparos.vyparos.attendance.dto;

import com.vyparos.vyparos.attendance.entity.AttendanceCorrection;
import com.vyparos.vyparos.identity.dto.UserSummaryDto;

import java.time.Instant;

public class AttendanceCorrectionResponse {

    private Long id;
    private Long attendanceRecordId;
    private UserSummaryDto correctedBy;
    private Instant previousClockIn;
    private Instant previousClockOut;
    private Instant newClockIn;
    private Instant newClockOut;
    private String reason;
    private Instant createdAt;

    public AttendanceCorrectionResponse() {}

    public static AttendanceCorrectionResponse fromEntity(AttendanceCorrection correction) {
        AttendanceCorrectionResponse response = new AttendanceCorrectionResponse();
        response.setId(correction.getId());
        response.setAttendanceRecordId(correction.getAttendanceRecord().getId());
        if (correction.getCorrectedByUser() != null) {
            response.setCorrectedBy(UserSummaryDto.fromEntity(correction.getCorrectedByUser()));
        }
        response.setPreviousClockIn(correction.getPreviousClockIn());
        response.setPreviousClockOut(correction.getPreviousClockOut());
        response.setNewClockIn(correction.getNewClockIn());
        response.setNewClockOut(correction.getNewClockOut());
        response.setReason(correction.getReason());
        response.setCreatedAt(correction.getCreatedAt());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAttendanceRecordId() {
        return attendanceRecordId;
    }

    public void setAttendanceRecordId(Long attendanceRecordId) {
        this.attendanceRecordId = attendanceRecordId;
    }

    public UserSummaryDto getCorrectedBy() {
        return correctedBy;
    }

    public void setCorrectedBy(UserSummaryDto correctedBy) {
        this.correctedBy = correctedBy;
    }

    public Instant getPreviousClockIn() {
        return previousClockIn;
    }

    public void setPreviousClockIn(Instant previousClockIn) {
        this.previousClockIn = previousClockIn;
    }

    public Instant getPreviousClockOut() {
        return previousClockOut;
    }

    public void setPreviousClockOut(Instant previousClockOut) {
        this.previousClockOut = previousClockOut;
    }

    public Instant getNewClockIn() {
        return newClockIn;
    }

    public void setNewClockIn(Instant newClockIn) {
        this.newClockIn = newClockIn;
    }

    public Instant getNewClockOut() {
        return newClockOut;
    }

    public void setNewClockOut(Instant newClockOut) {
        this.newClockOut = newClockOut;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
