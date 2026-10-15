package com.vyparos.vyparos.attendance.entity;

import com.vyparos.vyparos.identity.entity.User;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "attendance_corrections")
public class AttendanceCorrection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attendance_record_id", nullable = false)
    private AttendanceRecord attendanceRecord;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "corrected_by_user_id", nullable = false)
    private User correctedByUser;

    @Column(name = "previous_clock_in", nullable = false)
    private Instant previousClockIn;

    @Column(name = "previous_clock_out")
    private Instant previousClockOut;

    @Column(name = "new_clock_in", nullable = false)
    private Instant newClockIn;

    @Column(name = "new_clock_out")
    private Instant newClockOut;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public AttendanceCorrection() {}

    public AttendanceCorrection(AttendanceRecord attendanceRecord, User correctedByUser, Instant previousClockIn, Instant previousClockOut, Instant newClockIn, Instant newClockOut, String reason) {
        this.attendanceRecord = attendanceRecord;
        this.correctedByUser = correctedByUser;
        this.previousClockIn = previousClockIn;
        this.previousClockOut = previousClockOut;
        this.newClockIn = newClockIn;
        this.newClockOut = newClockOut;
        this.reason = reason;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttendanceRecord getAttendanceRecord() {
        return attendanceRecord;
    }

    public void setAttendanceRecord(AttendanceRecord attendanceRecord) {
        this.attendanceRecord = attendanceRecord;
    }

    public User getCorrectedByUser() {
        return correctedByUser;
    }

    public void setCorrectedByUser(User correctedByUser) {
        this.correctedByUser = correctedByUser;
    }

    public Instant getPreviousClockIn() {
        return previousClockIn;
    }

    public void setPreviousClockIn(Instant previousClockIn) {
        this.previousClockIn = previousClockIn;
    }

    public Instant getPreviousClockOut() {
        return previousClockOut;
    }

    public void setPreviousClockOut(Instant previousClockOut) {
        this.previousClockOut = previousClockOut;
    }

    public Instant getNewClockIn() {
        return newClockIn;
    }

    public void setNewClockIn(Instant newClockIn) {
        this.newClockIn = newClockIn;
    }

    public Instant getNewClockOut() {
        return newClockOut;
    }

    public void setNewClockOut(Instant newClockOut) {
        this.newClockOut = newClockOut;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
