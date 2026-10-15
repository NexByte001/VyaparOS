# Staff Attendance & Work Hours Rules

## 1. Clock-in Rules
1. **Authenticated Context**: Clock-in is invoked by the authenticated user for their active business membership.
2. **Duplicate Prevention**: A staff member cannot clock in if they already have an `OPEN` attendance record (i.e. `clock_out_time IS NULL`).
3. **Server Timestamping**: The clock-in time is determined strictly by the server (`Instant.now()`), never by client timestamps.

---

## 2. Clock-out Rules
1. **Session Match**: The server queries the single active `OPEN` session for the authenticated staff membership.
2. **Duplicate Prevention**: Attempting to clock out without an active open session returns `400 Bad Request`.
3. **Duration Calculation**: Server calculates duration in minutes: `Duration.between(clockInTime, clockOutTime).toMinutes()`.

---

## 3. Administrative Corrections
1. **Mandatory Reason**: An administrator modifying an attendance record must provide a non-empty `reason`.
2. **Audit Preservation**: The previous clock-in and clock-out timestamps are archived into `attendance_corrections` and logged in `audit_logs`.
3. **Immutability**: Original raw records are never deleted; status is updated to `CORRECTED`.
