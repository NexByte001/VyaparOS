package com.vyparos.vyparos.attendance.dto;

public class StaffAttendanceStats {

    private Long staffMembershipId;
    private String staffName;
    private String email;
    private String role;
    private long totalDays;
    private long daysPresent;
    private long daysAbsent;
    private long daysOnLeave;
    private long halfDays;
    private double attendancePercentage;
    private double totalHoursWorked;
    private long totalCustomersAttended;

    public StaffAttendanceStats() {}

    public Long getStaffMembershipId() {
        return staffMembershipId;
    }

    public void setStaffMembershipId(Long staffMembershipId) {
        this.staffMembershipId = staffMembershipId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(long totalDays) {
        this.totalDays = totalDays;
    }

    public long getDaysPresent() {
        return daysPresent;
    }

    public void setDaysPresent(long daysPresent) {
        this.daysPresent = daysPresent;
    }

    public long getDaysAbsent() {
        return daysAbsent;
    }

    public void setDaysAbsent(long daysAbsent) {
        this.daysAbsent = daysAbsent;
    }

    public long getDaysOnLeave() {
        return daysOnLeave;
    }

    public void setDaysOnLeave(long daysOnLeave) {
        this.daysOnLeave = daysOnLeave;
    }

    public long getHalfDays() {
        return halfDays;
    }

    public void setHalfDays(long halfDays) {
        this.halfDays = halfDays;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public double getTotalHoursWorked() {
        return totalHoursWorked;
    }

    public void setTotalHoursWorked(double totalHoursWorked) {
        this.totalHoursWorked = totalHoursWorked;
    }

    public long getTotalCustomersAttended() {
        return totalCustomersAttended;
    }

    public void setTotalCustomersAttended(long totalCustomersAttended) {
        this.totalCustomersAttended = totalCustomersAttended;
    }
}
