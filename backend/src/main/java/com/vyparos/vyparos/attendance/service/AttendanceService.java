package com.vyparos.vyparos.attendance.service;

import com.vyparos.vyparos.attendance.dto.*;
import com.vyparos.vyparos.attendance.entity.AttendanceStatus;
import com.vyparos.vyparos.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    AttendanceRecordResponse clockIn(Long businessId, Long userId, String notes);
    AttendanceRecordResponse clockOut(Long businessId, Long userId, String notes);
    List<AttendanceRecordResponse> getMyAttendance(Long businessId, Long userId);
    PageResponse<AttendanceRecordResponse> getTeamAttendance(
            Long businessId, Long staffMembershipId, AttendanceStatus status, Instant startDate, Instant endDate, Pageable pageable
    );
    AttendanceRecordResponse correctAttendance(Long businessId, Long attendanceRecordId, Long adminUserId, AttendanceCorrectionRequest request);

    AttendanceRecordResponse markAttendance(Long businessId, Long adminUserId, MarkAttendanceRequest request);
    AttendanceDashboardSummary getDashboardSummary(Long businessId, LocalDate date);
    List<DailyStaffAttendanceDto> getDailyAttendance(Long businessId, LocalDate date);
    List<AttendanceRecordResponse> getStaffAttendanceHistory(Long businessId, Long staffMembershipId, LocalDate startDate, LocalDate endDate);
    StaffAttendanceStats getStaffStats(Long businessId, Long staffMembershipId, LocalDate startDate, LocalDate endDate);
    List<StaffAttendanceStats> getMonthlyReport(Long businessId, LocalDate startDate, LocalDate endDate);
    String generateMonthlyReportCsv(Long businessId, LocalDate startDate, LocalDate endDate);
}

