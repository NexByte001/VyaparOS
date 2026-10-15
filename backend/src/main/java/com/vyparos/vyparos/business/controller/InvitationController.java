package com.vyparos.vyparos.business.controller;

import com.vyparos.vyparos.business.dto.MemberSummaryDto;
import com.vyparos.vyparos.business.dto.RegisterAndAcceptInviteRequest;
import com.vyparos.vyparos.business.dto.StaffInvitationDto;
import com.vyparos.vyparos.business.service.InvitationService;
import com.vyparos.vyparos.common.dto.ApiResponse;
import com.vyparos.vyparos.identity.dto.AuthResponse;
import com.vyparos.vyparos.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @GetMapping("/{token}")
    public ResponseEntity<ApiResponse<StaffInvitationDto>> getInvitation(
            @PathVariable String token) {
        StaffInvitationDto dto = invitationService.getInvitation(token);
        return ResponseEntity.ok(ApiResponse.ok("Invitation details retrieved", dto));
    }

    @PostMapping("/{token}/register-and-accept")
    public ResponseEntity<ApiResponse<AuthResponse>> registerAndAccept(
            @PathVariable String token,
            @Valid @RequestBody RegisterAndAcceptInviteRequest request) {
        AuthResponse authResponse = invitationService.registerAndAccept(token, request);
        return ResponseEntity.ok(ApiResponse.ok("Account created and invitation accepted successfully", authResponse));
    }

    @PostMapping("/{token}/accept")
    public ResponseEntity<ApiResponse<MemberSummaryDto>> acceptAuthenticated(
            @PathVariable String token,
            @AuthenticationPrincipal UserPrincipal principal) {
        MemberSummaryDto memberDto = invitationService.acceptAuthenticated(token, principal.getId());
        return ResponseEntity.ok(ApiResponse.ok("Invitation accepted and added to workspace", memberDto));
    }
}
