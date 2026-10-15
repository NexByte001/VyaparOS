package com.vyparos.vyparos.appointments.dto;

import java.time.Instant;

public class UpdateAppointmentRequest {

    private Long serviceId;
    private Long staffMembershipId;
    private Instant startTime;
    private String notes;

    public UpdateAppointmentRequest() {}

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getStaffMembershipId() {
        return staffMembershipId;
    }

    public void setStaffMembershipId(Long staffMembershipId) {
        this.staffMembershipId = staffMembershipId;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
