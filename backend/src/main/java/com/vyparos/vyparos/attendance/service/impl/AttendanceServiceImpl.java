package com.vyparos.vyparos.attendance.service.impl;

import com.vyparos.vyparos.appointments.repository.AppointmentRepository;
import com.vyparos.vyparos.attendance.dto.*;
import com.vyparos.vyparos.attendance.entity.AttendanceCorrection;
import com.vyparos.vyparos.attendance.entity.AttendanceRecord;
import com.vyparos.vyparos.attendance.entity.AttendanceStatus;
import com.vyparos.vyparos.attendance.repository.AttendanceCorrectionRepository;
import com.vyparos.vyparos.attendance.repository.AttendanceRecordRepository;
import com.vyparos.vyparos.attendance.service.AttendanceService;
import com.vyparos.vyparos.audit.service.AuditLogService;
import com.vyparos.vyparos.business.entity.BusinessMembership;
import com.vyparos.vyparos.business.repository.BusinessMembershipRepository;
import com.vyparos.vyparos.common.dto.PageResponse;
import com.vyparos.vyparos.exception.BadRequestException;
import com.vyparos.vyparos.exception.ResourceNotFoundException;
import com.vyparos.vyparos.identity.entity.User;
import com.vyparos.vyparos.identity.repository.UserRepository;
import com.vyparos.vyparos.security.TenantSecurityGuard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRecordRepository attendanceRepository;
    private final AttendanceCorrectionRepository correctionRepository;
    private final BusinessMembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    private final TenantSecurityGuard securityGuard;
    private final AppointmentRepository appointmentRepository;

    public AttendanceServiceImpl(
            AttendanceRecordRepository attendanceRepository,
            AttendanceCorrectionRepository correctionRepository,
            BusinessMembershipRepository membershipRepository,
            UserRepository userRepository,
            AuditLogService auditLogService,
            TenantSecurityGuard securityGuard,
            AppointmentRepository appointmentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.correctionRepository = correctionRepository;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
        this.securityGuard = securityGuard;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    @Transactional
    public AttendanceRecordResponse clockIn(Long businessId, Long userId, String notes) {
        BusinessMembership membership = securityGuard.requireMembership(businessId);

        // Check if there is already an open session
        attendanceRepository.findFirstByStaffMembershipIdAndStatusOrderByClockInTimeDesc(membership.getId(), AttendanceStatus.OPEN)
                .ifPresent(existing -> {
                    throw new BadRequestException("You already have an active open attendance session clocked in at " + existing.getClockInTime());
                });

        Instant now = Instant.now();
        LocalDate today = now.atZone(ZoneId.systemDefault()).toLocalDate();

        // Check if a record already exists for today (e.g. marked absent or previously checked out)
        Optional<AttendanceRecord> todayRecordOpt = attendanceRepository.findByBusinessIdAndStaffMembershipIdAndAttendanceDate(businessId, membership.getId(), today);

        AttendanceRecord record;
        if (todayRecordOpt.isPresent()) {
            record = todayRecordOpt.get();
            record.setClockInTime(now);
            record.setStatus(AttendanceStatus.OPEN);
            if (notes != null && !notes.isBlank()) {
                record.setNotes((record.getNotes() != null ? record.getNotes() + " | " : "") + notes.trim());
            }
        } else {
            record = new AttendanceRecord(
                    membership.getBusiness(),
                    membership,
                    now,
                    notes
            );
        }

        // Auto-count completed appointments today
        if (!Boolean.TRUE.equals(record.getIsManualCustomersCount())) {
            Instant dayStart = today.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant dayEnd = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            long count = appointmentRepository.countCompletedAppointmentsByStaffAndDate(businessId, membership.getId(), dayStart, dayEnd);
            record.setCustomersAttended((int) count);
        }

        AttendanceRecord saved = attendanceRepository.save(record);
        return AttendanceRecordResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public AttendanceRecordResponse clockOut(Long businessId, Long userId, String notes) {
        BusinessMembership membership = securityGuard.requireMembership(businessId);

        AttendanceRecord openRecord = attendanceRepository.findFirstByStaffMembershipIdAndStatusOrderByClockInTimeDesc(membership.getId(), AttendanceStatus.OPEN)
                .orElseThrow(() -> new BadRequestException("No active open attendance session found. You must clock in first."));

        Instant now = Instant.now();
        openRecord.setClockOutTime(now);

        long minutes = Math.max(0, Duration.between(openRecord.getClockInTime(), now).toMinutes());
        openRecord.setDurationMinutes((int) minutes);
        openRecord.setStatus(AttendanceStatus.COMPLETED);
        if (notes != null && !notes.isBlank()) {
            openRecord.setNotes((openRecord.getNotes() != null ? openRecord.getNotes() + " | " : "") + notes.trim());
        }

        // Auto-count completed customer appointments today
        if (!Boolean.TRUE.equals(openRecord.getIsManualCustomersCount())) {
            LocalDate attDate = openRecord.getAttendanceDate();
            Instant dayStart = attDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant dayEnd = attDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            long count = appointmentRepository.countCompletedAppointmentsByStaffAndDate(businessId, membership.getId(), dayStart, dayEnd);
            openRecord.setCustomersAttended((int) count);
        }

        AttendanceRecord saved = attendanceRepository.save(openRecord);
        return AttendanceRecordResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceRecordResponse> getMyAttendance(Long businessId, Long userId) {
        BusinessMembership membership = securityGuard.requireMembership(businessId);
        List<AttendanceRecord> records = attendanceRepository.findMyAttendanceHistory(membership.getId());
        return records.stream().map(AttendanceRecordResponse::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AttendanceRecordResponse> getTeamAttendance(
            Long businessId, Long staffMembershipId, AttendanceStatus status, Instant startDate, Instant endDate, Pageable pageable) {
        securityGuard.requireAdminOrOwner(businessId);
        Page<AttendanceRecord> page = attendanceRepository.findFilteredAttendance(
                businessId, staffMembershipId, status, startDate, endDate, pageable
        );
        return PageResponse.from(page.map(AttendanceRecordResponse::fromEntity));
    }

    @Override
    @Transactional
    public AttendanceRecordResponse correctAttendance(
            Long businessId, Long attendanceRecordId, Long adminUserId, AttendanceCorrectionRequest request) {
        BusinessMembership adminMembership = securityGuard.requireAdminOrOwner(businessId);
        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));

        AttendanceRecord record = attendanceRepository.findByIdAndBusinessId(attendanceRecordId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found in this business workspace"));

        if (request.getReason() == null || request.getReason().trim().isBlank()) {
            throw new BadRequestException("A mandatory justification reason is required for administrative attendance corrections");
        }

        Instant prevIn = record.getClockInTime();
        Instant prevOut = record.getClockOutTime();

        // Archive correction history
        AttendanceCorrection correction = new AttendanceCorrection(
                record,
                adminUser,
                prevIn,
                prevOut,
                request.getNewClockInTime(),
                request.getNewClockOutTime(),
                request.getReason().trim()
        );
        record.addCorrection(correction);
        correctionRepository.save(correction);

        // Update record
        record.setClockInTime(request.getNewClockInTime());
        record.setClockOutTime(request.getNewClockOutTime());
        if (request.getNewClockInTime() != null) {
            record.setAttendanceDate(request.getNewClockInTime().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        if (request.getNewClockOutTime() != null && request.getNewClockInTime() != null) {
            long minutes = Math.max(0, Duration.between(request.getNewClockInTime(), request.getNewClockOutTime()).toMinutes());
            record.setDurationMinutes((int) minutes);
        } else {
            record.setDurationMinutes(null);
        }
        record.setStatus(AttendanceStatus.CORRECTED);

        AttendanceRecord updated = attendanceRepository.save(record);

        // Emit audit log
        auditLogService.logAction(
                adminMembership.getBusiness(),
                adminUser,
                "CORRECT_ATTENDANCE",
                "AttendanceRecord",
                record.getId().toString(),
                "Previous: [" + prevIn + " to " + prevOut + "], New: [" + request.getNewClockInTime() + " to " + request.getNewClockOutTime() + "]",
                request.getReason().trim()
        );

        return AttendanceRecordResponse.fromEntity(updated);
    }

    @Override
    @Transactional
    public AttendanceRecordResponse markAttendance(Long businessId, Long adminUserId, MarkAttendanceRequest request) {
        BusinessMembership adminMembership = securityGuard.requireAdminOrOwner(businessId);
        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));

        BusinessMembership staff = membershipRepository.findById(request.getStaffMembershipId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found"));

        if (!staff.getBusiness().getId().equals(businessId)) {
            throw new BadRequestException("Staff member does not belong to this business workspace");
        }

        LocalDate targetDate = request.getDate() != null ? request.getDate() : LocalDate.now();
        Optional<AttendanceRecord> existingOpt = attendanceRepository.findByBusinessIdAndStaffMembershipIdAndAttendanceDate(
                businessId, staff.getId(), targetDate
        );

        AttendanceRecord record;
        if (existingOpt.isPresent()) {
            record = existingOpt.get();
        } else {
            record = new AttendanceRecord();
            record.setBusiness(adminMembership.getBusiness());
            record.setStaffMembership(staff);
            record.setAttendanceDate(targetDate);
        }

        record.setStatus(request.getStatus());

        if (request.getNotes() != null) {
            record.setNotes(request.getNotes().trim());
        }

        // Configure times based on status
        if (request.getStatus() == AttendanceStatus.ABSENT || request.getStatus() == AttendanceStatus.ON_LEAVE) {
            record.setClockInTime(targetDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            record.setClockOutTime(null);
            record.setDurationMinutes(0);
        } else {
            if (request.getClockInTime() != null) {
                record.setClockInTime(request.getClockInTime());
            } else if (record.getClockInTime() == null) {
                record.setClockInTime(targetDate.atTime(9, 0).atZone(ZoneId.systemDefault()).toInstant());
            }

            if (request.getClockOutTime() != null) {
                record.setClockOutTime(request.getClockOutTime());
            }

            if (request.getDurationMinutes() != null) {
                record.setDurationMinutes(request.getDurationMinutes());
            } else if (record.getClockInTime() != null && record.getClockOutTime() != null) {
                long minutes = Math.max(0, Duration.between(record.getClockInTime(), record.getClockOutTime()).toMinutes());
                record.setDurationMinutes((int) minutes);
            } else if (request.getStatus() == AttendanceStatus.HALF_DAY) {
                record.setDurationMinutes(240); // 4 hours default
            }
        }

        // Customers attended
        if (request.getCustomersAttended() != null) {
            record.setCustomersAttended(request.getCustomersAttended());
            record.setIsManualCustomersCount(true);
        } else if (!Boolean.TRUE.equals(record.getIsManualCustomersCount())) {
            Instant dayStart = targetDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant dayEnd = targetDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            long count = appointmentRepository.countCompletedAppointmentsByStaffAndDate(businessId, staff.getId(), dayStart, dayEnd);
            record.setCustomersAttended((int) count);
        }

        AttendanceRecord saved = attendanceRepository.save(record);

        auditLogService.logAction(
                adminMembership.getBusiness(),
                adminUser,
                "MARK_ATTENDANCE",
                "AttendanceRecord",
                saved.getId().toString(),
                "Staff: " + (staff.getUser() != null ? staff.getUser().getFullName() : staff.getId()) + ", Status: " + request.getStatus() + ", Date: " + targetDate,
                request.getNotes()
        );

        return AttendanceRecordResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceDashboardSummary getDashboardSummary(Long businessId, LocalDate date) {
        securityGuard.requireMembership(businessId);
        LocalDate targetDate = date != null ? date : LocalDate.now();

        List<BusinessMembership> activeStaff = membershipRepository.findAllByBusinessId(businessId).stream()
                .filter(BusinessMembership::isActive)
                .toList();
        long totalStaff = activeStaff.size();

        List<AttendanceRecord> records = attendanceRepository.findByBusinessIdAndAttendanceDate(businessId, targetDate);

        long workingNow = records.stream()
                .filter(r -> r.getStatus() == AttendanceStatus.OPEN)
                .count();

        long presentToday = records.stream()
                .filter(r -> r.getStatus() == AttendanceStatus.PRESENT
                        || r.getStatus() == AttendanceStatus.COMPLETED
                        || r.getStatus() == AttendanceStatus.OPEN
                        || r.getStatus() == AttendanceStatus.CORRECTED)
                .count();

        long absentToday = records.stream()
                .filter(r -> r.getStatus() == AttendanceStatus.ABSENT)
                .count();

        long onLeaveToday = records.stream()
                .filter(r -> r.getStatus() == AttendanceStatus.ON_LEAVE)
                .count();

        long halfDaysToday = records.stream()
                .filter(r -> r.getStatus() == AttendanceStatus.HALF_DAY)
                .count();

        Instant now = Instant.now();
        long totalMinutes = 0;
        for (AttendanceRecord r : records) {
            if (r.getStatus() == AttendanceStatus.OPEN && r.getClockInTime() != null) {
                totalMinutes += Math.max(0, Duration.between(r.getClockInTime(), now).toMinutes());
            } else if (r.getDurationMinutes() != null) {
                totalMinutes += r.getDurationMinutes();
            }
        }
        double totalHoursWorked = Math.round((totalMinutes / 60.0) * 10.0) / 10.0;

        long customersAttendedToday = records.stream()
                .mapToLong(AttendanceRecord::getCustomersAttended)
                .sum();

        return new AttendanceDashboardSummary(
                totalStaff,
                presentToday,
                absentToday,
                workingNow,
                onLeaveToday,
                halfDaysToday,
                totalHoursWorked,
                customersAttendedToday
        );
    }

    @Override
    @Transactional
    public List<DailyStaffAttendanceDto> getDailyAttendance(Long businessId, LocalDate date) {
        securityGuard.requireMembership(businessId);
        LocalDate targetDate = date != null ? date : LocalDate.now();

        List<BusinessMembership> activeStaff = membershipRepository.findAllByBusinessId(businessId).stream()
                .filter(BusinessMembership::isActive)
                .sorted(Comparator.comparing(bm -> bm.getUser() != null ? bm.getUser().getFullName() : ""))
                .toList();

        List<AttendanceRecord> records = attendanceRepository.findByBusinessIdAndAttendanceDate(businessId, targetDate);
        Map<Long, AttendanceRecord> recordMap = records.stream()
                .collect(Collectors.toMap(r -> r.getStaffMembership().getId(), r -> r, (a, b) -> a));

        Instant dayStart = targetDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant dayEnd = targetDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<DailyStaffAttendanceDto> results = new ArrayList<>();

        for (BusinessMembership staff : activeStaff) {
            DailyStaffAttendanceDto dto = new DailyStaffAttendanceDto();
            dto.setStaffMembershipId(staff.getId());
            dto.setStaffName(staff.getUser() != null ? staff.getUser().getFullName() : "Staff #" + staff.getId());
            dto.setEmail(staff.getUser() != null ? staff.getUser().getEmail() : "");
            dto.setRole(staff.getRole() != null ? staff.getRole().name() : "STAFF");
            dto.setDate(targetDate);

            AttendanceRecord record = recordMap.get(staff.getId());
            if (record != null) {
                dto.setAttendanceRecordId(record.getId());
                dto.setStatus(record.getStatus());
                dto.setClockInTime(record.getClockInTime());
                dto.setClockOutTime(record.getClockOutTime());
                dto.setDurationMinutes(record.getDurationMinutes());
                dto.setNotes(record.getNotes());
                dto.setIsManualCustomersCount(record.getIsManualCustomersCount());

                // Auto-refresh completed customers count if not manually overridden
                if (!Boolean.TRUE.equals(record.getIsManualCustomersCount())) {
                    long completedCount = appointmentRepository.countCompletedAppointmentsByStaffAndDate(
                            businessId, staff.getId(), dayStart, dayEnd
                    );
                    record.setCustomersAttended((int) completedCount);
                    attendanceRepository.save(record);
                }
                dto.setCustomersAttended(record.getCustomersAttended());
            } else {
                // Not marked yet today
                dto.setStatus(null);
                dto.setClockInTime(null);
                dto.setClockOutTime(null);
                dto.setDurationMinutes(null);
                dto.setNotes(null);
                dto.setIsManualCustomersCount(false);

                // Still show completed customers attended today
                long completedCount = appointmentRepository.countCompletedAppointmentsByStaffAndDate(
                        businessId, staff.getId(), dayStart, dayEnd
                );
                dto.setCustomersAttended((int) completedCount);
            }

            results.add(dto);
        }

        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceRecordResponse> getStaffAttendanceHistory(
            Long businessId, Long staffMembershipId, LocalDate startDate, LocalDate endDate) {
        securityGuard.requireMembership(businessId);

        LocalDate start = startDate != null ? startDate : LocalDate.now().minusMonths(1);
        LocalDate end = endDate != null ? endDate : LocalDate.now();

        List<AttendanceRecord> records = attendanceRepository.findByBusinessIdAndStaffMembershipIdAndAttendanceDateBetween(
                businessId, staffMembershipId, start, end
        );

        return records.stream().map(AttendanceRecordResponse::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StaffAttendanceStats getStaffStats(
            Long businessId, Long staffMembershipId, LocalDate startDate, LocalDate endDate) {
        securityGuard.requireMembership(businessId);

        BusinessMembership staff = membershipRepository.findById(staffMembershipId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found"));

        LocalDate start = startDate != null ? startDate : LocalDate.now().minusMonths(1);
        LocalDate end = endDate != null ? endDate : LocalDate.now();

        List<AttendanceRecord> records = attendanceRepository.findByBusinessIdAndStaffMembershipIdAndAttendanceDateBetween(
                businessId, staffMembershipId, start, end
        );

        return calculateStaffStats(staff, records);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffAttendanceStats> getMonthlyReport(
            Long businessId, LocalDate startDate, LocalDate endDate) {
        securityGuard.requireAdminOrOwner(businessId);

        LocalDate start = startDate != null ? startDate : LocalDate.now().withDayOfMonth(1);
        LocalDate end = endDate != null ? endDate : LocalDate.now();

        List<BusinessMembership> activeStaff = membershipRepository.findAllByBusinessId(businessId).stream()
                .filter(BusinessMembership::isActive)
                .sorted(Comparator.comparing(bm -> bm.getUser() != null ? bm.getUser().getFullName() : ""))
                .toList();

        List<AttendanceRecord> allRecords = attendanceRepository.findByBusinessIdAndAttendanceDateBetween(
                businessId, start, end
        );

        Map<Long, List<AttendanceRecord>> recordsByStaff = allRecords.stream()
                .collect(Collectors.groupingBy(r -> r.getStaffMembership().getId()));

        List<StaffAttendanceStats> statsList = new ArrayList<>();
        for (BusinessMembership staff : activeStaff) {
            List<AttendanceRecord> staffRecords = recordsByStaff.getOrDefault(staff.getId(), Collections.emptyList());
            statsList.add(calculateStaffStats(staff, staffRecords));
        }

        return statsList;
    }

    @Override
    @Transactional(readOnly = true)
    public String generateMonthlyReportCsv(Long businessId, LocalDate startDate, LocalDate endDate) {
        List<StaffAttendanceStats> report = getMonthlyReport(businessId, startDate, endDate);

        StringBuilder sb = new StringBuilder();
        sb.append("Staff Name,Email,Role,Total Days Recorded,Days Present,Days Absent,Days On Leave,Half Days,Attendance %,Total Hours Worked,Total Customers Attended\n");

        for (StaffAttendanceStats s : report) {
            sb.append("\"").append(escapeCsv(s.getStaffName())).append("\",")
              .append("\"").append(escapeCsv(s.getEmail())).append("\",")
              .append("\"").append(escapeCsv(s.getRole())).append("\",")
              .append(s.getTotalDays()).append(",")
              .append(s.getDaysPresent()).append(",")
              .append(s.getDaysAbsent()).append(",")
              .append(s.getDaysOnLeave()).append(",")
              .append(s.getHalfDays()).append(",")
              .append(s.getAttendancePercentage()).append("%,")
              .append(s.getTotalHoursWorked()).append(",")
              .append(s.getTotalCustomersAttended()).append("\n");
        }

        return sb.toString();
    }

    private StaffAttendanceStats calculateStaffStats(BusinessMembership staff, List<AttendanceRecord> records) {
        StaffAttendanceStats stats = new StaffAttendanceStats();
        stats.setStaffMembershipId(staff.getId());
        stats.setStaffName(staff.getUser() != null ? staff.getUser().getFullName() : "Staff #" + staff.getId());
        stats.setEmail(staff.getUser() != null ? staff.getUser().getEmail() : "");
        stats.setRole(staff.getRole() != null ? staff.getRole().name() : "STAFF");

        long present = 0;
        long absent = 0;
        long leave = 0;
        long half = 0;
        long totalMinutes = 0;
        long totalCustomers = 0;

        for (AttendanceRecord r : records) {
            if (r.getStatus() == AttendanceStatus.PRESENT
                    || r.getStatus() == AttendanceStatus.COMPLETED
                    || r.getStatus() == AttendanceStatus.CORRECTED
                    || r.getStatus() == AttendanceStatus.OPEN) {
                present++;
            } else if (r.getStatus() == AttendanceStatus.ABSENT) {
                absent++;
            } else if (r.getStatus() == AttendanceStatus.ON_LEAVE) {
                leave++;
            } else if (r.getStatus() == AttendanceStatus.HALF_DAY) {
                half++;
            }

            if (r.getDurationMinutes() != null) {
                totalMinutes += r.getDurationMinutes();
            }
            if (r.getCustomersAttended() != null) {
                totalCustomers += r.getCustomersAttended();
            }
        }

        long totalDays = records.size();
        stats.setTotalDays(totalDays);
        stats.setDaysPresent(present);
        stats.setDaysAbsent(absent);
        stats.setDaysOnLeave(leave);
        stats.setHalfDays(half);
        stats.setTotalHoursWorked(Math.round((totalMinutes / 60.0) * 10.0) / 10.0);
        stats.setTotalCustomersAttended(totalCustomers);

        double pct = 0.0;
        if (totalDays > 0) {
            pct = ((present + (half * 0.5)) / (double) totalDays) * 100.0;
            pct = Math.round(pct * 10.0) / 10.0;
        }
        stats.setAttendancePercentage(pct);

        return stats;
    }

    private String escapeCsv(String val) {
        if (val == null) return "";
        return val.replace("\"", "\"\"");
    }
}
