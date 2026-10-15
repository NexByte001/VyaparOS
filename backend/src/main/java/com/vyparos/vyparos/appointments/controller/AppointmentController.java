package com.vyparos.vyparos.appointments.controller;

import com.vyparos.vyparos.appointments.dto.AppointmentResponse;
import com.vyparos.vyparos.appointments.dto.AppointmentStatusUpdateRequest;
import com.vyparos.vyparos.appointments.dto.CreateAppointmentRequest;
import com.vyparos.vyparos.appointments.dto.UpdateAppointmentRequest;
import com.vyparos.vyparos.appointments.entity.AppointmentStatus;
import com.vyparos.vyparos.appointments.service.AppointmentService;
import com.vyparos.vyparos.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/businesses/{businessId}/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointments(
            @PathVariable Long businessId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) Long staffId,
            @RequestParam(required = false) Long customerId) {
        List<AppointmentResponse> appointments = appointmentService.getAppointments(
                businessId, startDate, endDate, status, staffId, customerId
        );
        return ResponseEntity.ok(ApiResponse.ok("Appointments retrieved successfully", appointments));
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentById(
            @PathVariable Long businessId,
            @PathVariable Long appointmentId) {
        AppointmentResponse appointment = appointmentService.getAppointmentById(businessId, appointmentId);
        return ResponseEntity.ok(ApiResponse.ok("Appointment details retrieved", appointment));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(
            @PathVariable Long businessId,
            @Valid @RequestBody CreateAppointmentRequest request) {
        AppointmentResponse created = appointmentService.createAppointment(businessId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Appointment booked successfully", created));
    }

    @PatchMapping("/{appointmentId}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateAppointment(
            @PathVariable Long businessId,
            @PathVariable Long appointmentId,
            @RequestBody UpdateAppointmentRequest request) {
        AppointmentResponse updated = appointmentService.updateAppointment(businessId, appointmentId, request);
        return ResponseEntity.ok(ApiResponse.ok("Appointment updated successfully", updated));
    }

    @RequestMapping(value = "/{appointmentId}/status", method = {RequestMethod.POST, RequestMethod.PATCH})
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateAppointmentStatus(
            @PathVariable Long businessId,
            @PathVariable Long appointmentId,
            @Valid @RequestBody AppointmentStatusUpdateRequest request) {
        AppointmentResponse updated = appointmentService.updateAppointmentStatus(businessId, appointmentId, request);
        return ResponseEntity.ok(ApiResponse.ok("Appointment status updated", updated));
    }

    @PostMapping("/{appointmentId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelAppointment(
            @PathVariable Long businessId,
            @PathVariable Long appointmentId) {
        appointmentService.cancelAppointment(businessId, appointmentId);
        return ResponseEntity.ok(ApiResponse.ok("Appointment cancelled successfully"));
    }
}
