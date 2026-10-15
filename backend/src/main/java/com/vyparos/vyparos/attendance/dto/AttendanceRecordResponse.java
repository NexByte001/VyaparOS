package com.vyparos.vyparos.attendance.dto;

import com.vyparos.vyparos.attendance.entity.AttendanceRecord;
import com.vyparos.vyparos.attendance.entity.AttendanceStatus;
import com.vyparos.vyparos.business.dto.MemberSummaryDto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class AttendanceRecordResponse {

    private Long id;
    private Long businessId;
    private MemberSummaryDto staffMember;
    private LocalDate attendanceDate;
    private Instant clockInTime;
    private Instant clockOutTime;
    private Integer durationMinutes;
    private AttendanceStatus status;
    private Integer customersAttended;
    private Boolean isManualCustomersCount;
    private String notes;
    private List<AttendanceCorrectionResponse> corrections;
    private Instant createdAt;
    private Instant updatedAt;

    public AttendanceRecordResponse() {}

    public static AttendanceRecordResponse fromEntity(AttendanceRecord record) {
        AttendanceRecordResponse response = new AttendanceRecordResponse();
        response.setId(record.getId());
        response.setBusinessId(record.getBusiness().getId());
        if (record.getStaffMembership() != null) {
            response.setStaffMember(MemberSummaryDto.fromEntity(record.getStaffMembership()));
        }
        response.setAttendanceDate(record.getAttendanceDate());
        response.setClockInTime(record.getClockInTime());
        response.setClockOutTime(record.getClockOutTime());
        response.setDurationMinutes(record.getDurationMinutes());
        response.setStatus(record.getStatus());
        response.setCustomersAttended(record.getCustomersAttended());
        response.setIsManualCustomersCount(record.getIsManualCustomersCount());
        response.setNotes(record.getNotes());
        if (record.getCorrections() != null) {
            response.setCorrections(record.getCorrections().stream().map(AttendanceCorrectionResponse::fromEntity).toList());
        }
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        return response;
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

    public MemberSummaryDto getStaffMember() {
        return staffMember;
    }

    public void setStaffMember(MemberSummaryDto staffMember) {
        this.staffMember = staffMember;
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

    public List<AttendanceCorrectionResponse> getCorrections() {
        return corrections;
    }

    public void setCorrections(List<AttendanceCorrectionResponse> corrections) {
        this.corrections = corrections;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Integer getCustomersAttended() {
        return customersAttended;
    }

    public void setCustomersAttended(Integer customersAttended) {
        this.customersAttended = customersAttended;
    }

    public Boolean getIsManualCustomersCount() {
        return isManualCustomersCount;
    }

    public void setIsManualCustomersCount(Boolean isManualCustomersCount) {
        this.isManualCustomersCount = isManualCustomersCount;
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
