package com.vyparos.vyparos.appointments.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class CreateAppointmentRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Service ID is required")
    private Long serviceId;

    private Long staffMembershipId;

    @NotNull(message = "Start time is required")
    private Instant startTime;

    private String notes;

    public CreateAppointmentRequest() {}

    public CreateAppointmentRequest(Long customerId, Long serviceId, Long staffMembershipId, Instant startTime, String notes) {
        this.customerId = customerId;
        this.serviceId = serviceId;
        this.staffMembershipId = staffMembershipId;
        this.startTime = startTime;
        this.notes = notes;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

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
