package com.vyparos.vyparos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vyparos.vyparos.attendance.dto.AttendanceCorrectionRequest;
import com.vyparos.vyparos.attendance.dto.MarkAttendanceRequest;
import com.vyparos.vyparos.attendance.entity.AttendanceStatus;
import com.vyparos.vyparos.business.dto.CreateBusinessRequest;
import com.vyparos.vyparos.identity.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AttendanceAndAuditTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldTrackAttendancePreventDuplicateClockInAndAuditCorrections() throws Exception {
        // 1. Setup user & business
        RegisterRequest userReq = new RegisterRequest("attendowner@test.com", "pass12345", "Sunil Owner", "9876543210");
        String userRes = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String token = objectMapper.readTree(userRes).path("data").path("token").asText();

        CreateBusinessRequest bizReq = new CreateBusinessRequest("Sunil Fitness", "Gym", "INR", "Asia/Kolkata", "Indiranagar", "9876543210", "sunil@gym.com");
        String bizRes = mockMvc.perform(post("/api/businesses")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bizReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long bizId = objectMapper.readTree(bizRes).path("data").path("id").asLong();

        // 2. Clock In
        String clockInRes = mockMvc.perform(post("/api/businesses/" + bizId + "/attendance/clock-in")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.status").value("OPEN"))
                .andExpect(jsonPath("$.data.clockInTime").isNotEmpty())
                .andReturn().getResponse().getContentAsString();
        long attId = objectMapper.readTree(clockInRes).path("data").path("id").asLong();

        // 3. Duplicate Clock In -> MUST FAIL with 400 Bad Request
        mockMvc.perform(post("/api/businesses/" + bizId + "/attendance/clock-in")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));

        // 4. Clock Out
        mockMvc.perform(post("/api/businesses/" + bizId + "/attendance/clock-out")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("COMPLETED"))
                .andExpect(jsonPath("$.data.clockOutTime").isNotEmpty());

        // 5. Fetch Personal Attendance (/attendance/me)
        mockMvc.perform(get("/api/businesses/" + bizId + "/attendance/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(attId));

        // 6. Admin Attendance Correction without Reason -> MUST FAIL with 400
        Instant newIn = Instant.now().minus(8, ChronoUnit.HOURS);
        Instant newOut = Instant.now();
        AttendanceCorrectionRequest badCorrection = new AttendanceCorrectionRequest(newIn, newOut, "");
        mockMvc.perform(post("/api/businesses/" + bizId + "/admin/attendance/" + attId + "/correct")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badCorrection)))
                .andExpect(status().isBadRequest());

        // 7. Admin Attendance Correction with Valid Reason (480 minutes / 8 hours)
        AttendanceCorrectionRequest goodCorrection = new AttendanceCorrectionRequest(
                newIn, newOut, "Employee forgot to clock out at the end of the shift"
        );
        mockMvc.perform(post("/api/businesses/" + bizId + "/admin/attendance/" + attId + "/correct")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(goodCorrection)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("CORRECTED"))
                .andExpect(jsonPath("$.data.durationMinutes").value(480))
                .andExpect(jsonPath("$.data.corrections[0].reason").value("Employee forgot to clock out at the end of the shift"));

        // 8. Verify Audit Logs endpoint (/admin/audit-logs)
        mockMvc.perform(get("/api/businesses/" + bizId + "/admin/audit-logs")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].action").value("CORRECT_ATTENDANCE"))
                .andExpect(jsonPath("$.data.content[0].entityType").value("AttendanceRecord"))
                .andExpect(jsonPath("$.data.content[0].reason").value("Employee forgot to clock out at the end of the shift"));

        // 9. Fetch Staff Membership ID for admin tests
        String membersRes = mockMvc.perform(get("/api/businesses/" + bizId + "/members")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        long staffMemberId = objectMapper.readTree(membersRes).path("data").get(0).path("membershipId").asLong();

        // 10. Mark Attendance as PRESENT for a specific date
        LocalDate testDate = LocalDate.now().minusDays(1);
        MarkAttendanceRequest markReq = new MarkAttendanceRequest();
        markReq.setStaffMembershipId(staffMemberId);
        markReq.setDate(testDate);
        markReq.setStatus(AttendanceStatus.PRESENT);
        markReq.setDurationMinutes(480);
        markReq.setNotes("Regular shift completed");

        String markRes = mockMvc.perform(post("/api/businesses/" + bizId + "/attendance/mark")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(markReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PRESENT"))
                .andExpect(jsonPath("$.data.durationMinutes").value(480))
                .andReturn().getResponse().getContentAsString();
        long markRecordId = objectMapper.readTree(markRes).path("data").path("id").asLong();

        // 11. Prevent Duplicate Record: Updating attendance for same staff on same date updates the existing record
        MarkAttendanceRequest updateMarkReq = new MarkAttendanceRequest();
        updateMarkReq.setStaffMembershipId(staffMemberId);
        updateMarkReq.setDate(testDate);
        updateMarkReq.setStatus(AttendanceStatus.HALF_DAY);
        updateMarkReq.setDurationMinutes(240);
        updateMarkReq.setNotes("Corrected to half day");

        mockMvc.perform(post("/api/businesses/" + bizId + "/attendance/mark")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMarkReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(markRecordId))
                .andExpect(jsonPath("$.data.status").value("HALF_DAY"))
                .andExpect(jsonPath("$.data.durationMinutes").value(240));

        // 12. Manual Customer Count Override
        MarkAttendanceRequest overrideReq = new MarkAttendanceRequest();
        overrideReq.setStaffMembershipId(staffMemberId);
        overrideReq.setDate(testDate);
        overrideReq.setStatus(AttendanceStatus.PRESENT);
        overrideReq.setCustomersAttended(7);
        overrideReq.setNotes("Manual override VIP rush");

        mockMvc.perform(post("/api/businesses/" + bizId + "/attendance/mark")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(overrideReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.customersAttended").value(7))
                .andExpect(jsonPath("$.data.isManualCustomersCount").value(true));

        // 13. Verify Daily Staff Attendance Table Endpoint
        mockMvc.perform(get("/api/businesses/" + bizId + "/attendance/daily?date=" + testDate)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].staffMembershipId").value(staffMemberId))
                .andExpect(jsonPath("$.data[0].status").value("PRESENT"))
                .andExpect(jsonPath("$.data[0].customersAttended").value(7));

        // 14. Verify Dashboard Summary KPIs
        mockMvc.perform(get("/api/businesses/" + bizId + "/attendance/dashboard-summary?date=" + testDate)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalStaff").value(1))
                .andExpect(jsonPath("$.data.presentToday").value(1))
                .andExpect(jsonPath("$.data.customersAttendedToday").value(7));

        // 15. Verify Staff Attendance History & Stats Endpoints
        mockMvc.perform(get("/api/businesses/" + bizId + "/attendance/staff/" + staffMemberId + "/history")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());

        mockMvc.perform(get("/api/businesses/" + bizId + "/attendance/staff/" + staffMemberId + "/stats")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.staffMembershipId").value(staffMemberId))
                .andExpect(jsonPath("$.data.attendancePercentage").isNumber())
                .andExpect(jsonPath("$.data.totalCustomersAttended").value(7));

        // 16. Verify Monthly Report & CSV Export
        mockMvc.perform(get("/api/businesses/" + bizId + "/attendance/monthly-report")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].staffMembershipId").value(staffMemberId));

        mockMvc.perform(get("/api/businesses/" + bizId + "/attendance/monthly-report/csv")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("attendance_report_")))
                .andExpect(content().contentTypeCompatibleWith("text/csv"));
    }
}
