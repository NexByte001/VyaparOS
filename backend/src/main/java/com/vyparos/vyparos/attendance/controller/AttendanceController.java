package com.vyparos.vyparos.attendance.controller;

import com.vyparos.vyparos.attendance.dto.*;
import com.vyparos.vyparos.attendance.entity.AttendanceStatus;
import com.vyparos.vyparos.attendance.service.AttendanceService;
import com.vyparos.vyparos.common.dto.ApiResponse;
import com.vyparos.vyparos.common.dto.PageResponse;
import com.vyparos.vyparos.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/businesses/{businessId}")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/attendance/clock-in")
    public ResponseEntity<ApiResponse<AttendanceRecordResponse>> clockIn(
            @PathVariable Long businessId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody(required = false) Map<String, String> body) {
        String notes = body != null ? body.get("notes") : null;
        AttendanceRecordResponse response = attendanceService.clockIn(businessId, principal.getId(), notes);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Clock-in recorded successfully", response));
    }

    @PostMapping("/attendance/clock-out")
    public ResponseEntity<ApiResponse<AttendanceRecordResponse>> clockOut(
            @PathVariable Long businessId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody(required = false) Map<String, String> body) {
        String notes = body != null ? body.get("notes") : null;
        AttendanceRecordResponse response = attendanceService.clockOut(businessId, principal.getId(), notes);
        return ResponseEntity.ok(ApiResponse.ok("Clock-out recorded successfully", response));
    }

    @GetMapping("/attendance/me")
    public ResponseEntity<ApiResponse<List<AttendanceRecordResponse>>> getMyAttendance(
            @PathVariable Long businessId,
            @AuthenticationPrincipal UserPrincipal principal) {
        List<AttendanceRecordResponse> records = attendanceService.getMyAttendance(businessId, principal.getId());
        return ResponseEntity.ok(ApiResponse.ok("Personal attendance history retrieved", records));
    }

    @GetMapping("/admin/attendance")
    public ResponseEntity<ApiResponse<PageResponse<AttendanceRecordResponse>>> getTeamAttendance(
            @PathVariable Long businessId,
            @RequestParam(required = false) Long staffId,
            @RequestParam(required = false) AttendanceStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
            @PageableDefault(size = 30, sort = "clockInTime", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<AttendanceRecordResponse> response = attendanceService.getTeamAttendance(
                businessId, staffId, status, startDate, endDate, pageable
        );
        return ResponseEntity.ok(ApiResponse.ok("Team attendance records retrieved", response));
    }

    @PostMapping("/admin/attendance/{attendanceId}/correct")
    public ResponseEntity<ApiResponse<AttendanceRecordResponse>> correctAttendance(
            @PathVariable Long businessId,
            @PathVariable Long attendanceId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AttendanceCorrectionRequest request) {
        AttendanceRecordResponse response = attendanceService.correctAttendance(
                businessId, attendanceId, principal.getId(), request
        );
        return ResponseEntity.ok(ApiResponse.ok("Attendance corrected and audit log recorded", response));
    }

    @GetMapping("/attendance/dashboard-summary")
    public ResponseEntity<ApiResponse<AttendanceDashboardSummary>> getDashboardSummary(
            @PathVariable Long businessId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        AttendanceDashboardSummary summary = attendanceService.getDashboardSummary(businessId, date);
        return ResponseEntity.ok(ApiResponse.ok("Attendance dashboard summary retrieved", summary));
    }

    @GetMapping("/attendance/daily")
    public ResponseEntity<ApiResponse<List<DailyStaffAttendanceDto>>> getDailyAttendance(
            @PathVariable Long businessId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<DailyStaffAttendanceDto> daily = attendanceService.getDailyAttendance(businessId, date);
        return ResponseEntity.ok(ApiResponse.ok("Daily attendance records retrieved", daily));
    }

    @PostMapping({"/attendance/mark", "/admin/attendance/mark"})
    public ResponseEntity<ApiResponse<AttendanceRecordResponse>> markAttendance(
            @PathVariable Long businessId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody MarkAttendanceRequest request) {
        AttendanceRecordResponse response = attendanceService.markAttendance(businessId, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Attendance marked successfully", response));
    }

    @GetMapping("/attendance/staff/{staffMembershipId}/history")
    public ResponseEntity<ApiResponse<List<AttendanceRecordResponse>>> getStaffHistory(
            @PathVariable Long businessId,
            @PathVariable Long staffMembershipId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<AttendanceRecordResponse> history = attendanceService.getStaffAttendanceHistory(
                businessId, staffMembershipId, startDate, endDate
        );
        return ResponseEntity.ok(ApiResponse.ok("Staff attendance history retrieved", history));
    }

    @GetMapping("/attendance/staff/{staffMembershipId}/stats")
    public ResponseEntity<ApiResponse<StaffAttendanceStats>> getStaffStats(
            @PathVariable Long businessId,
            @PathVariable Long staffMembershipId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        StaffAttendanceStats stats = attendanceService.getStaffStats(
                businessId, staffMembershipId, startDate, endDate
        );
        return ResponseEntity.ok(ApiResponse.ok("Staff attendance stats retrieved", stats));
    }

    @GetMapping({"/attendance/monthly-report", "/admin/attendance/monthly-report"})
    public ResponseEntity<ApiResponse<List<StaffAttendanceStats>>> getMonthlyReport(
            @PathVariable Long businessId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<StaffAttendanceStats> report = attendanceService.getMonthlyReport(businessId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.ok("Monthly attendance report retrieved", report));
    }

    @GetMapping({"/attendance/monthly-report/csv", "/admin/attendance/monthly-report/csv"})
    public ResponseEntity<String> exportMonthlyReportCsv(
            @PathVariable Long businessId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        String csv = attendanceService.generateMonthlyReportCsv(businessId, startDate, endDate);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attendance_report_" + businessId + ".csv")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csv);
    }
}
