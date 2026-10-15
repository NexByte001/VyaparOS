package com.vyparos.vyparos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vyparos.vyparos.appointments.dto.AppointmentStatusUpdateRequest;
import com.vyparos.vyparos.appointments.dto.CreateAppointmentRequest;
import com.vyparos.vyparos.appointments.entity.AppointmentStatus;
import com.vyparos.vyparos.business.dto.CreateBusinessRequest;
import com.vyparos.vyparos.customers.dto.CustomerRequest;
import com.vyparos.vyparos.identity.dto.RegisterRequest;
import com.vyparos.vyparos.services.dto.ServiceRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AppointmentSchedulingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAppointmentAndDetectSchedulingConflicts() throws Exception {
        // 1. Register & setup business
        RegisterRequest userReq = new RegisterRequest("salonowner@test.com", "pass12345", "Kiran Salon", "9876543210");
        String userRes = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String token = objectMapper.readTree(userRes).path("data").path("token").asText();

        CreateBusinessRequest bizReq = new CreateBusinessRequest("Kiran Spa", "Spa", "INR", "Asia/Kolkata", "Brigade Rd", "9876543210", "kiran@spa.com");
        String bizRes = mockMvc.perform(post("/api/businesses")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bizReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long bizId = objectMapper.readTree(bizRes).path("data").path("id").asLong();

        // Fetch owner's membership ID
        String membersRes = mockMvc.perform(get("/api/businesses/" + bizId + "/members")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        long staffMembershipId = objectMapper.readTree(membersRes).path("data").get(0).path("membershipId").asLong();

        // 2. Create customer & service (60 minutes, price = 800)
        CustomerRequest custReq = new CustomerRequest("Anita Roy", "9123456780", "anita@gmail.com", "Koramangala", "Regular");
        String custRes = mockMvc.perform(post("/api/businesses/" + bizId + "/customers")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(custReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long custId = objectMapper.readTree(custRes).path("data").path("id").asLong();

        ServiceRequest servReq = new ServiceRequest("Aromatherapy Massage", "60 min relaxing massage", 60, new BigDecimal("800.00"));
        String servRes = mockMvc.perform(post("/api/businesses/" + bizId + "/services")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long servId = objectMapper.readTree(servRes).path("data").path("id").asLong();

        // 3. Book first appointment (10:00 to 11:00)
        Instant baseTime = Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.HOURS);
        CreateAppointmentRequest appt1Req = new CreateAppointmentRequest(custId, servId, staffMembershipId, baseTime, "First booking");
        String appt1Res = mockMvc.perform(post("/api/businesses/" + bizId + "/appointments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appt1Req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.status").value("SCHEDULED"))
                .andExpect(jsonPath("$.data.priceSnapshot").value(800.00))
                .andReturn().getResponse().getContentAsString();
        long appt1Id = objectMapper.readTree(appt1Res).path("data").path("id").asLong();

        // 4. Attempt overlapping booking for same staff (10:30 to 11:30) -> MUST FAIL with 409 Conflict
        Instant overlappingTime = baseTime.plus(30, ChronoUnit.MINUTES);
        CreateAppointmentRequest conflictReq = new CreateAppointmentRequest(custId, servId, staffMembershipId, overlappingTime, "Conflict booking");
        mockMvc.perform(post("/api/businesses/" + bizId + "/appointments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conflictReq)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("CONFLICT"));

        // 5. Update appointment 1 status to COMPLETED
        AppointmentStatusUpdateRequest statusReq = new AppointmentStatusUpdateRequest(AppointmentStatus.COMPLETED);
        mockMvc.perform(post("/api/businesses/" + bizId + "/appointments/" + appt1Id + "/status")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("COMPLETED"));

        // 6. Attempt backdated appointment (2 days in the past) -> MUST FAIL with 400 Bad Request
        Instant pastTime = Instant.now().minus(2, ChronoUnit.DAYS);
        CreateAppointmentRequest pastReq = new CreateAppointmentRequest(custId, servId, staffMembershipId, pastTime, "Backdated booking attempt");
        mockMvc.perform(post("/api/businesses/" + bizId + "/appointments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pastReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));

        // 7. Retrieve appointment list -> MUST include appointment 1
        mockMvc.perform(get("/api/businesses/" + bizId + "/appointments")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(appt1Id))
                .andExpect(jsonPath("$.data[0].status").value("COMPLETED"));
    }
}
