package com.vyparos.vyparos.attendance.repository;

import com.vyparos.vyparos.attendance.entity.AttendanceCorrection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceCorrectionRepository extends JpaRepository<AttendanceCorrection, Long> {
    List<AttendanceCorrection> findAllByAttendanceRecordIdOrderByCreatedAtDesc(Long attendanceRecordId);
}
