package com.vyparos.vyparos.attendance.dto;

public class AttendanceDashboardSummary {

    private long totalStaff;
    private long presentToday;
    private long absentToday;
    private long workingNow;
    private long onLeaveToday;
    private long halfDaysToday;
    private double totalHoursWorkedToday;
    private long customersAttendedToday;

    public AttendanceDashboardSummary() {}

    public AttendanceDashboardSummary(long totalStaff, long presentToday, long absentToday, long workingNow, long onLeaveToday, long halfDaysToday, double totalHoursWorkedToday, long customersAttendedToday) {
        this.totalStaff = totalStaff;
        this.presentToday = presentToday;
        this.absentToday = absentToday;
        this.workingNow = workingNow;
        this.onLeaveToday = onLeaveToday;
        this.halfDaysToday = halfDaysToday;
        this.totalHoursWorkedToday = totalHoursWorkedToday;
        this.customersAttendedToday = customersAttendedToday;
    }

    public long getTotalStaff() {
        return totalStaff;
    }

    public void setTotalStaff(long totalStaff) {
        this.totalStaff = totalStaff;
    }

    public long getPresentToday() {
        return presentToday;
    }

    public void setPresentToday(long presentToday) {
        this.presentToday = presentToday;
    }

    public long getAbsentToday() {
        return absentToday;
    }

    public void setAbsentToday(long absentToday) {
        this.absentToday = absentToday;
    }

    public long getWorkingNow() {
        return workingNow;
    }

    public void setWorkingNow(long workingNow) {
        this.workingNow = workingNow;
    }

    public long getOnLeaveToday() {
        return onLeaveToday;
    }

    public void setOnLeaveToday(long onLeaveToday) {
        this.onLeaveToday = onLeaveToday;
    }

    public long getHalfDaysToday() {
        return halfDaysToday;
    }

    public void setHalfDaysToday(long halfDaysToday) {
        this.halfDaysToday = halfDaysToday;
    }

    public double getTotalHoursWorkedToday() {
        return totalHoursWorkedToday;
    }

    public void setTotalHoursWorkedToday(double totalHoursWorkedToday) {
        this.totalHoursWorkedToday = totalHoursWorkedToday;
    }

    public long getCustomersAttendedToday() {
        return customersAttendedToday;
    }

    public void setCustomersAttendedToday(long customersAttendedToday) {
        this.customersAttendedToday = customersAttendedToday;
    }
}
