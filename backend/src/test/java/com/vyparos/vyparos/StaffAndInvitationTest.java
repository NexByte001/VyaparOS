package com.vyparos.vyparos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vyparos.vyparos.business.dto.AddMemberRequest;
import com.vyparos.vyparos.business.dto.CreateBusinessRequest;
import com.vyparos.vyparos.business.dto.RegisterAndAcceptInviteRequest;
import com.vyparos.vyparos.business.entity.BusinessRole;
import com.vyparos.vyparos.identity.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StaffAndInvitationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String registerAndGetToken(String email, String name) throws Exception {
        RegisterRequest req = new RegisterRequest(email, "password123", name, "9876543210");
        MvcResult res = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(res.getResponse().getContentAsString())
                .path("data").path("token").asText();
    }

    private Long createWorkspace(String token, String bizName) throws Exception {
        CreateBusinessRequest req = new CreateBusinessRequest(bizName, "Retail", "INR", "Asia/Kolkata", null, null, null);
        MvcResult res = mockMvc.perform(post("/api/businesses")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(res.getResponse().getContentAsString())
                .path("data").path("id").asLong();
    }

    @Test
    void shouldAddExistingRegisteredUserDirectlyToWorkspace() throws Exception {
        // 1. Setup owner & existing staff user
        String ownerToken = registerAndGetToken("owner_test1@vyparos.com", "Owner One");
        Long bizId = createWorkspace(ownerToken, "Alpha Enterprises");

        registerAndGetToken("existing_staff@vyparos.com", "Sunil Verma");

        // 2. Add existing user
        AddMemberRequest addReq = new AddMemberRequest("existing_staff@vyparos.com", BusinessRole.STAFF);
        addReq.setJobTitle("Sales Associate");
        addReq.setDepartment("Sales");

        mockMvc.perform(post("/api/businesses/" + bizId + "/members")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.actionType").value("ADDED"))
                .andExpect(jsonPath("$.data.member.email").value("existing_staff@vyparos.com"))
                .andExpect(jsonPath("$.data.member.role").value("STAFF"))
                .andExpect(jsonPath("$.data.member.jobTitle").value("Sales Associate"));

        // 3. Attempting to add same user again should return 409 Conflict
        mockMvc.perform(post("/api/businesses/" + bizId + "/members")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addReq)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldCreateInvitationForUnregisteredUserAndAllowAcceptance() throws Exception {
        // 1. Setup workspace
        String ownerToken = registerAndGetToken("owner_invite@vyparos.com", "Invite Owner");
        Long bizId = createWorkspace(ownerToken, "Apex Health & Wellness");

        // 2. Invite unregistered email
        AddMemberRequest inviteReq = new AddMemberRequest("new_employee@vyparos.com", BusinessRole.STAFF);
        inviteReq.setFullName("Priya Sharma");
        inviteReq.setPhone("9123456789");
        inviteReq.setJobTitle("Lead Therapist");
        inviteReq.setDepartment("Operations");

        MvcResult inviteRes = mockMvc.perform(post("/api/businesses/" + bizId + "/members")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inviteReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.actionType").value("INVITED"))
                .andExpect(jsonPath("$.data.invitation.status").value("PENDING"))
                .andExpect(jsonPath("$.data.invitation.invitationToken").isNotEmpty())
                .andReturn();

        String token = objectMapper.readTree(inviteRes.getResponse().getContentAsString())
                .path("data").path("invitation").path("invitationToken").asText();

        // 3. Public endpoint inspects invitation details
        mockMvc.perform(get("/api/invitations/" + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("new_employee@vyparos.com"))
                .andExpect(jsonPath("$.data.role").value("STAFF"))
                .andExpect(jsonPath("$.data.businessName").value("Apex Health & Wellness"));

        // 4. Duplicate pending invitation should be rejected with 409 Conflict
        mockMvc.perform(post("/api/businesses/" + bizId + "/members")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inviteReq)))
                .andExpect(status().isConflict());

        // 5. New user completes onboarding via register-and-accept
        RegisterAndAcceptInviteRequest acceptReq = new RegisterAndAcceptInviteRequest(
                "Priya Sharma",
                "securePass789",
                "9123456789"
        );

        mockMvc.perform(post("/api/invitations/" + token + "/register-and-accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(acceptReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.user.email").value("new_employee@vyparos.com"))
                .andExpect(jsonPath("$.data.memberships", hasSize(1)))
                .andExpect(jsonPath("$.data.memberships[0].businessName").value("Apex Health & Wellness"))
                .andExpect(jsonPath("$.data.memberships[0].role").value("STAFF"));

        // 6. Attempting to reuse the accepted invitation token should be rejected
        mockMvc.perform(post("/api/invitations/" + token + "/register-and-accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(acceptReq)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldRevokeInvitationAndPreventAcceptance() throws Exception {
        String ownerToken = registerAndGetToken("owner_revoke@vyparos.com", "Revoke Owner");
        Long bizId = createWorkspace(ownerToken, "Revoke Studios");

        AddMemberRequest inviteReq = new AddMemberRequest("temp_contractor@vyparos.com", BusinessRole.STAFF);
        inviteReq.setFullName("Contractor One");

        MvcResult inviteRes = mockMvc.perform(post("/api/businesses/" + bizId + "/members")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inviteReq)))
                .andExpect(status().isCreated())
                .andReturn();

        Long inviteId = objectMapper.readTree(inviteRes.getResponse().getContentAsString())
                .path("data").path("invitation").path("id").asLong();
        String token = objectMapper.readTree(inviteRes.getResponse().getContentAsString())
                .path("data").path("invitation").path("invitationToken").asText();

        // Revoke invitation
        mockMvc.perform(delete("/api/businesses/" + bizId + "/invitations/" + inviteId)
                        .header("Authorization", "Bearer " + ownerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("REVOKED"));

        // Attempting to register and accept revoked invite should fail with 400 Bad Request
        RegisterAndAcceptInviteRequest acceptReq = new RegisterAndAcceptInviteRequest("Contractor One", "pass12345", null);
        mockMvc.perform(post("/api/invitations/" + token + "/register-and-accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(acceptReq)))
                .andExpect(status().isBadRequest());
    }
}
