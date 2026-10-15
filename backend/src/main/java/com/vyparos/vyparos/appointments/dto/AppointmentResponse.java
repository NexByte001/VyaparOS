package com.vyparos.vyparos.appointments.dto;

import com.vyparos.vyparos.appointments.entity.Appointment;
import com.vyparos.vyparos.appointments.entity.AppointmentStatus;
import com.vyparos.vyparos.business.dto.MemberSummaryDto;
import com.vyparos.vyparos.customers.dto.CustomerResponse;
import com.vyparos.vyparos.services.dto.ServiceResponse;

import java.math.BigDecimal;
import java.time.Instant;

public class AppointmentResponse {

    private Long id;
    private Long businessId;
    private CustomerResponse customer;
    private ServiceResponse service;
    private MemberSummaryDto staff;
    private Instant startTime;
    private Instant endTime;
    private AppointmentStatus status;
    private BigDecimal priceSnapshot;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;

    public AppointmentResponse() {}

    public static AppointmentResponse fromEntity(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setBusinessId(appointment.getBusiness().getId());
        response.setCustomer(CustomerResponse.fromEntity(appointment.getCustomer()));
        response.setService(ServiceResponse.fromEntity(appointment.getService()));
        if (appointment.getStaffMembership() != null) {
            response.setStaff(MemberSummaryDto.fromEntity(appointment.getStaffMembership()));
        }
        response.setStartTime(appointment.getStartTime());
        response.setEndTime(appointment.getEndTime());
        response.setStatus(appointment.getStatus());
        response.setPriceSnapshot(appointment.getPriceSnapshot());
        response.setNotes(appointment.getNotes());
        response.setCreatedAt(appointment.getCreatedAt());
        response.setUpdatedAt(appointment.getUpdatedAt());
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

    public CustomerResponse getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerResponse customer) {
        this.customer = customer;
    }

    public ServiceResponse getService() {
        return service;
    }

    public void setService(ServiceResponse service) {
        this.service = service;
    }

    public MemberSummaryDto getStaff() {
        return staff;
    }

    public void setStaff(MemberSummaryDto staff) {
        this.staff = staff;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public BigDecimal getPriceSnapshot() {
        return priceSnapshot;
    }

    public void setPriceSnapshot(BigDecimal priceSnapshot) {
        this.priceSnapshot = priceSnapshot;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
