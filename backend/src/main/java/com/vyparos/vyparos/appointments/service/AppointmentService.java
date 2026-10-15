package com.vyparos.vyparos.appointments.service;

import com.vyparos.vyparos.appointments.dto.AppointmentResponse;
import com.vyparos.vyparos.appointments.dto.AppointmentStatusUpdateRequest;
import com.vyparos.vyparos.appointments.dto.CreateAppointmentRequest;
import com.vyparos.vyparos.appointments.dto.UpdateAppointmentRequest;
import com.vyparos.vyparos.appointments.entity.AppointmentStatus;

import java.time.Instant;
import java.util.List;

public interface AppointmentService {
    List<AppointmentResponse> getAppointments(
            Long businessId,
            Instant startDate,
            Instant endDate,
            AppointmentStatus status,
            Long staffMembershipId,
            Long customerId
    );
    AppointmentResponse getAppointmentById(Long businessId, Long appointmentId);
    AppointmentResponse createAppointment(Long businessId, CreateAppointmentRequest request);
    AppointmentResponse updateAppointment(Long businessId, Long appointmentId, UpdateAppointmentRequest request);
    AppointmentResponse updateAppointmentStatus(Long businessId, Long appointmentId, AppointmentStatusUpdateRequest request);
    void cancelAppointment(Long businessId, Long appointmentId);
}
