package com.vyparos.vyparos.appointments.repository;

import com.vyparos.vyparos.appointments.entity.Appointment;
import com.vyparos.vyparos.appointments.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByIdAndBusinessId(Long id, Long businessId);

    @Query("SELECT a FROM Appointment a " +
           "JOIN FETCH a.customer c " +
           "JOIN FETCH a.service s " +
           "LEFT JOIN FETCH a.staffMembership sm " +
           "LEFT JOIN FETCH sm.user " +
           "WHERE a.business.id = :businessId " +
           "AND (:startDate IS NULL OR a.startTime >= :startDate) " +
           "AND (:endDate IS NULL OR a.startTime <= :endDate) " +
           "AND (:status IS NULL OR a.status = :status) " +
           "AND (:staffMembershipId IS NULL OR (sm.id IS NOT NULL AND sm.id = :staffMembershipId)) " +
           "AND (:customerId IS NULL OR c.id = :customerId) " +
           "ORDER BY a.startTime ASC")
    List<Appointment> findFilteredAppointments(
            @Param("businessId") Long businessId,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("status") AppointmentStatus status,
            @Param("staffMembershipId") Long staffMembershipId,
            @Param("customerId") Long customerId
    );

    @Query("SELECT COUNT(a) > 0 FROM Appointment a " +
           "WHERE a.business.id = :businessId " +
           "AND a.staffMembership.id = :staffMembershipId " +
           "AND a.status NOT IN ('CANCELLED', 'NO_SHOW') " +
           "AND (:excludeId IS NULL OR a.id != :excludeId) " +
           "AND (a.startTime < :endTime AND a.endTime > :startTime)")
    boolean existsOverlappingAppointment(
            @Param("businessId") Long businessId,
            @Param("staffMembershipId") Long staffMembershipId,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime,
            @Param("excludeId") Long excludeId
    );

    long countByBusinessIdAndStartTimeBetween(Long businessId, Instant start, Instant end);
    long countByBusinessIdAndStatusAndStartTimeBetween(Long businessId, AppointmentStatus status, Instant start, Instant end);

    @Query("SELECT COUNT(a) FROM Appointment a " +
           "WHERE a.business.id = :businessId " +
           "AND a.staffMembership.id = :staffMembershipId " +
           "AND a.status = 'COMPLETED' " +
           "AND a.startTime >= :start AND a.startTime <= :end")
    long countCompletedAppointmentsByStaffAndDate(
            @Param("businessId") Long businessId,
            @Param("staffMembershipId") Long staffMembershipId,
            @Param("start") Instant start,
            @Param("end") Instant end
    );
}
