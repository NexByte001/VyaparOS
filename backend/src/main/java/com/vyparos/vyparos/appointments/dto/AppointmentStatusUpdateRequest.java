package com.vyparos.vyparos.appointments.dto;

import com.vyparos.vyparos.appointments.entity.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public class AppointmentStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private AppointmentStatus status;

    public AppointmentStatusUpdateRequest() {}

    public AppointmentStatusUpdateRequest(AppointmentStatus status) {
        this.status = status;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}
