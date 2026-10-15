package com.vyparos.vyparos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vyparos.vyparos.business.dto.CreateBusinessRequest;
import com.vyparos.vyparos.customers.dto.CustomerRequest;
import com.vyparos.vyparos.identity.dto.LoginRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CustomerAndServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldEnforceTenantIsolationOnCustomersAndServices() throws Exception {
        // 1. Register User 1
        RegisterRequest user1 = new RegisterRequest("bizowner1@test.com", "pass12345", "Alice Owner", "1111111111");
        String user1Res = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String token1 = objectMapper.readTree(user1Res).path("data").path("token").asText();

        // 2. Create Business 1 for User 1
        CreateBusinessRequest bizReq1 = new CreateBusinessRequest("Alice Salon", "Salon", "INR", "Asia/Kolkata", "123 Main St", "1111111111", "alice@salon.com");
        String biz1Res = mockMvc.perform(post("/api/businesses")
                        .header("Authorization", "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bizReq1)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long bizId1 = objectMapper.readTree(biz1Res).path("data").path("id").asLong();

        // 3. User 1 adds Customer in Business 1
        CustomerRequest custReq = new CustomerRequest("John Doe", "9998887770", "john@gmail.com", "Apt 4B", "Prefers morning slots");
        String custRes = mockMvc.perform(post("/api/businesses/" + bizId1 + "/customers")
                        .header("Authorization", "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(custReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andReturn().getResponse().getContentAsString();
        long custId = objectMapper.readTree(custRes).path("data").path("id").asLong();

        // 4. User 1 adds Service in Business 1
        ServiceRequest servReq = new ServiceRequest("Haircut & Styling", "Classic cut", 45, new BigDecimal("499.00"));
        mockMvc.perform(post("/api/businesses/" + bizId1 + "/services")
                        .header("Authorization", "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Haircut & Styling"))
                .andExpect(jsonPath("$.data.price").value(499.00));

        // 5. Register User 2 (different business owner)
        RegisterRequest user2 = new RegisterRequest("bizowner2@test.com", "pass12345", "Bob Owner", "2222222222");
        String user2Res = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String token2 = objectMapper.readTree(user2Res).path("data").path("token").asText();

        // 6. User 2 attempts unauthorized access to Business 1's customers -> Expect 404/403
        mockMvc.perform(get("/api/businesses/" + bizId1 + "/customers")
                        .header("Authorization", "Bearer " + token2))
                .andExpect(status().isNotFound());

        // 7. User 2 attempts unauthorized direct access to Customer ID in Business 1 -> Expect 404/403
        mockMvc.perform(get("/api/businesses/" + bizId1 + "/customers/" + custId)
                        .header("Authorization", "Bearer " + token2))
                .andExpect(status().isNotFound());
    }
}
