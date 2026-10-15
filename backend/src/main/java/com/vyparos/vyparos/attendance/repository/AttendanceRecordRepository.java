package com.vyparos.vyparos.attendance.repository;

import com.vyparos.vyparos.attendance.entity.AttendanceRecord;
import com.vyparos.vyparos.attendance.entity.AttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {

    Optional<AttendanceRecord> findByIdAndBusinessId(Long id, Long businessId);

    Optional<AttendanceRecord> findFirstByStaffMembershipIdAndStatusOrderByClockInTimeDesc(Long staffMembershipId, AttendanceStatus status);

    Optional<AttendanceRecord> findByBusinessIdAndStaffMembershipIdAndAttendanceDate(Long businessId, Long staffMembershipId, LocalDate attendanceDate);

    @Query(value = "SELECT a FROM AttendanceRecord a " +
           "JOIN FETCH a.staffMembership sm " +
           "JOIN FETCH sm.user u " +
           "WHERE a.business.id = :businessId " +
           "AND (:staffMembershipId IS NULL OR a.staffMembership.id = :staffMembershipId) " +
           "AND (:status IS NULL OR a.status = :status) " +
           "AND (:startDate IS NULL OR a.clockInTime >= :startDate) " +
           "AND (:endDate IS NULL OR a.clockInTime <= :endDate)",
           countQuery = "SELECT COUNT(a) FROM AttendanceRecord a " +
           "WHERE a.business.id = :businessId " +
           "AND (:staffMembershipId IS NULL OR a.staffMembership.id = :staffMembershipId) " +
           "AND (:status IS NULL OR a.status = :status) " +
           "AND (:startDate IS NULL OR a.clockInTime >= :startDate) " +
           "AND (:endDate IS NULL OR a.clockInTime <= :endDate)")
    Page<AttendanceRecord> findFilteredAttendance(
            @Param("businessId") Long businessId,
            @Param("staffMembershipId") Long staffMembershipId,
            @Param("status") AttendanceStatus status,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            Pageable pageable
    );

    @Query("SELECT a FROM AttendanceRecord a " +
           "JOIN FETCH a.staffMembership sm " +
           "JOIN FETCH sm.user u " +
           "WHERE a.business.id = :businessId " +
           "AND a.attendanceDate = :attendanceDate " +
           "ORDER BY a.clockInTime ASC")
    List<AttendanceRecord> findByBusinessIdAndAttendanceDate(
            @Param("businessId") Long businessId,
            @Param("attendanceDate") LocalDate attendanceDate
    );

    @Query("SELECT a FROM AttendanceRecord a " +
           "JOIN FETCH a.staffMembership sm " +
           "JOIN FETCH sm.user u " +
           "WHERE a.business.id = :businessId " +
           "AND a.attendanceDate BETWEEN :startDate AND :endDate " +
           "ORDER BY a.attendanceDate DESC, a.clockInTime ASC")
    List<AttendanceRecord> findByBusinessIdAndAttendanceDateBetween(
            @Param("businessId") Long businessId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT a FROM AttendanceRecord a " +
           "JOIN FETCH a.staffMembership sm " +
           "JOIN FETCH sm.user u " +
           "WHERE a.business.id = :businessId " +
           "AND a.staffMembership.id = :staffMembershipId " +
           "AND a.attendanceDate BETWEEN :startDate AND :endDate " +
           "ORDER BY a.attendanceDate DESC")
    List<AttendanceRecord> findByBusinessIdAndStaffMembershipIdAndAttendanceDateBetween(
            @Param("businessId") Long businessId,
            @Param("staffMembershipId") Long staffMembershipId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT a FROM AttendanceRecord a " +
           "WHERE a.staffMembership.id = :staffMembershipId " +
           "ORDER BY a.clockInTime DESC")
    List<AttendanceRecord> findMyAttendanceHistory(@Param("staffMembershipId") Long staffMembershipId);

    @Query("SELECT COALESCE(SUM(a.durationMinutes), 0) FROM AttendanceRecord a " +
           "WHERE a.business.id = :businessId " +
           "AND (:staffMembershipId IS NULL OR a.staffMembership.id = :staffMembershipId) " +
           "AND (:startDate IS NULL OR a.clockInTime >= :startDate) " +
           "AND (:endDate IS NULL OR a.clockInTime <= :endDate)")
    long sumDurationMinutes(
            @Param("businessId") Long businessId,
            @Param("staffMembershipId") Long staffMembershipId,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate
    );

    @Query("SELECT COALESCE(SUM(a.customersAttended), 0) FROM AttendanceRecord a " +
           "WHERE a.business.id = :businessId " +
           "AND a.attendanceDate = :attendanceDate")
    long sumCustomersAttended(
            @Param("businessId") Long businessId,
            @Param("attendanceDate") LocalDate attendanceDate
    );

    long countByBusinessIdAndAttendanceDateAndStatus(Long businessId, LocalDate attendanceDate, AttendanceStatus status);

    long countByBusinessIdAndStatus(Long businessId, AttendanceStatus status);
}
