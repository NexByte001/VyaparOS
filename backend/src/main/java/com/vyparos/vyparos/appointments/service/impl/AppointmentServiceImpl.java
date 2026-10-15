package com.vyparos.vyparos.appointments.service.impl;

import com.vyparos.vyparos.appointments.dto.AppointmentResponse;
import com.vyparos.vyparos.appointments.dto.AppointmentStatusUpdateRequest;
import com.vyparos.vyparos.appointments.dto.CreateAppointmentRequest;
import com.vyparos.vyparos.appointments.dto.UpdateAppointmentRequest;
import com.vyparos.vyparos.appointments.entity.Appointment;
import com.vyparos.vyparos.appointments.entity.AppointmentStatus;
import com.vyparos.vyparos.appointments.repository.AppointmentRepository;
import com.vyparos.vyparos.appointments.service.AppointmentService;
import com.vyparos.vyparos.business.entity.BusinessMembership;
import com.vyparos.vyparos.business.repository.BusinessMembershipRepository;
import com.vyparos.vyparos.customers.entity.Customer;
import com.vyparos.vyparos.customers.repository.CustomerRepository;
import com.vyparos.vyparos.exception.BadRequestException;
import com.vyparos.vyparos.exception.ConflictException;
import com.vyparos.vyparos.exception.ResourceNotFoundException;
import com.vyparos.vyparos.security.TenantSecurityGuard;
import com.vyparos.vyparos.services.entity.ServiceEntity;
import com.vyparos.vyparos.services.repository.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final ServiceRepository serviceRepository;
    private final BusinessMembershipRepository membershipRepository;
    private final TenantSecurityGuard securityGuard;

    public AppointmentServiceImpl(
            AppointmentRepository appointmentRepository,
            CustomerRepository customerRepository,
            ServiceRepository serviceRepository,
            BusinessMembershipRepository membershipRepository,
            TenantSecurityGuard securityGuard) {
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.serviceRepository = serviceRepository;
        this.membershipRepository = membershipRepository;
        this.securityGuard = securityGuard;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointments(
            Long businessId,
            Instant startDate,
            Instant endDate,
            AppointmentStatus status,
            Long staffMembershipId,
            Long customerId) {
        securityGuard.requireMembership(businessId);
        List<Appointment> appointments = appointmentRepository.findFilteredAppointments(
                businessId, startDate, endDate, status, staffMembershipId, customerId
        );
        return appointments.stream().map(AppointmentResponse::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long businessId, Long appointmentId) {
        securityGuard.requireMembership(businessId);
        Appointment appointment = appointmentRepository.findByIdAndBusinessId(appointmentId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found in this business workspace"));
        return AppointmentResponse.fromEntity(appointment);
    }

    @Override
    @Transactional
    public AppointmentResponse createAppointment(Long businessId, CreateAppointmentRequest request) {
        BusinessMembership callerMembership = securityGuard.requireMembership(businessId);

        Customer customer = customerRepository.findByIdAndBusinessId(request.getCustomerId(), businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found in this business workspace"));

        ServiceEntity service = serviceRepository.findByIdAndBusinessId(request.getServiceId(), businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found in this business workspace"));

        if (!service.isActive()) {
            throw new BadRequestException("Cannot book an inactive service");
        }

        BusinessMembership staffMember = null;
        if (request.getStaffMembershipId() != null) {
            staffMember = membershipRepository.findById(request.getStaffMembershipId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff member not found"));

            if (!staffMember.getBusiness().getId().equals(businessId) || !staffMember.isActive()) {
                throw new BadRequestException("Staff member does not belong to this active business workspace");
            }
        }

        Instant startTime = request.getStartTime();
        if (startTime == null) {
            throw new BadRequestException("Appointment start date and time is required");
        }

        // Timezone-aware backdate restriction: prevent bookings in the past
        // Allow a 2-minute buffer for network/form latency
        Instant nowThreshold = Instant.now().minus(Duration.ofMinutes(2));
        if (startTime.isBefore(nowThreshold)) {
            throw new BadRequestException("Cannot create backdated appointments. Please select a current or future appointment time.");
        }

        Instant endTime = startTime.plus(Duration.ofMinutes(service.getDurationMinutes()));

        // Conflict check for staff member
        if (staffMember != null) {
            boolean hasConflict = appointmentRepository.existsOverlappingAppointment(
                    businessId, staffMember.getId(), startTime, endTime, null
            );
            if (hasConflict) {
                throw new ConflictException("The selected staff member is already booked for an overlapping appointment during this time window");
            }
        }

        Appointment appointment = new Appointment(
                callerMembership.getBusiness(),
                customer,
                service,
                staffMember,
                startTime,
                endTime,
                service.getPrice(), // Snapshot service price
                request.getNotes()
        );

        Appointment saved = appointmentRepository.save(appointment);
        return AppointmentResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public AppointmentResponse updateAppointment(Long businessId, Long appointmentId, UpdateAppointmentRequest request) {
        securityGuard.requireMembership(businessId);

        Appointment appointment = appointmentRepository.findByIdAndBusinessId(appointmentId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found in this business workspace"));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED || appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot modify a cancelled or completed appointment");
        }

        if (request.getServiceId() != null && !request.getServiceId().equals(appointment.getService().getId())) {
            ServiceEntity newService = serviceRepository.findByIdAndBusinessId(request.getServiceId(), businessId)
                    .orElseThrow(() -> new ResourceNotFoundException("Service not found in this business workspace"));
            appointment.setService(newService);
            appointment.setPriceSnapshot(newService.getPrice());
        }

        if (request.getStaffMembershipId() != null) {
            BusinessMembership staffMember = membershipRepository.findById(request.getStaffMembershipId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff member not found"));
            if (!staffMember.getBusiness().getId().equals(businessId) || !staffMember.isActive()) {
                throw new BadRequestException("Staff member does not belong to this active business workspace");
            }
            appointment.setStaffMembership(staffMember);
        }

        if (request.getStartTime() != null) {
            if (request.getStartTime().isBefore(Instant.now().minus(Duration.ofMinutes(2)))) {
                throw new BadRequestException("Cannot reschedule appointment to a time in the past.");
            }
            appointment.setStartTime(request.getStartTime());
        }

        Instant calculatedEnd = appointment.getStartTime().plus(Duration.ofMinutes(appointment.getService().getDurationMinutes()));
        appointment.setEndTime(calculatedEnd);

        // Re-check staff collision if staff assigned
        if (appointment.getStaffMembership() != null) {
            boolean hasConflict = appointmentRepository.existsOverlappingAppointment(
                    businessId,
                    appointment.getStaffMembership().getId(),
                    appointment.getStartTime(),
                    appointment.getEndTime(),
                    appointment.getId()
            );
            if (hasConflict) {
                throw new ConflictException("The selected staff member has an overlapping booking during this updated schedule");
            }
        }

        if (request.getNotes() != null) {
            appointment.setNotes(request.getNotes());
        }

        Appointment updated = appointmentRepository.save(appointment);
        return AppointmentResponse.fromEntity(updated);
    }

    @Override
    @Transactional
    public AppointmentResponse updateAppointmentStatus(Long businessId, Long appointmentId, AppointmentStatusUpdateRequest request) {
        securityGuard.requireMembership(businessId);

        Appointment appointment = appointmentRepository.findByIdAndBusinessId(appointmentId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found in this business workspace"));

        appointment.setStatus(request.getStatus());
        Appointment saved = appointmentRepository.save(appointment);
        return AppointmentResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public void cancelAppointment(Long businessId, Long appointmentId) {
        securityGuard.requireMembership(businessId);

        Appointment appointment = appointmentRepository.findByIdAndBusinessId(appointmentId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found in this business workspace"));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }
}
