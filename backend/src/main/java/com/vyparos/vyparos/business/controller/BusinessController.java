package com.vyparos.vyparos.business.controller;

import com.vyparos.vyparos.business.dto.*;
import com.vyparos.vyparos.business.service.BusinessService;
import com.vyparos.vyparos.common.dto.ApiResponse;
import com.vyparos.vyparos.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BusinessDetailDto>> createBusiness(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateBusinessRequest request) {
        BusinessDetailDto created = businessService.createBusiness(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Business workspace created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BusinessMembershipDto>>> getUserBusinesses(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<BusinessMembershipDto> businesses = businessService.getUserBusinesses(principal.getId());
        return ResponseEntity.ok(ApiResponse.ok("User businesses retrieved", businesses));
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<ApiResponse<BusinessDetailDto>> getBusinessDetails(
            @PathVariable Long businessId) {
        BusinessDetailDto details = businessService.getBusinessDetails(businessId);
        return ResponseEntity.ok(ApiResponse.ok("Business details retrieved", details));
    }

    @PatchMapping("/{businessId}")
    public ResponseEntity<ApiResponse<BusinessDetailDto>> updateBusiness(
            @PathVariable Long businessId,
            @Valid @RequestBody UpdateBusinessRequest request) {
        BusinessDetailDto updated = businessService.updateBusiness(businessId, request);
        return ResponseEntity.ok(ApiResponse.ok("Business workspace updated", updated));
    }

    @GetMapping("/{businessId}/members")
    public ResponseEntity<ApiResponse<List<MemberSummaryDto>>> getMembers(
            @PathVariable Long businessId) {
        List<MemberSummaryDto> members = businessService.getMembers(businessId);
        return ResponseEntity.ok(ApiResponse.ok("Staff members retrieved", members));
    }

    @PostMapping("/{businessId}/members")
    public ResponseEntity<ApiResponse<AddMemberResponse>> addMember(
            @PathVariable Long businessId,
            @Valid @RequestBody AddMemberRequest request) {
        AddMemberResponse response = businessService.addMember(businessId, request);
        HttpStatus status = "INVITED".equalsIgnoreCase(response.getActionType()) ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.ok(response.getMessage(), response));
    }

    @PatchMapping("/{businessId}/members/{memberId}")
    public ResponseEntity<ApiResponse<MemberSummaryDto>> updateMember(
            @PathVariable Long businessId,
            @PathVariable Long memberId,
            @Valid @RequestBody UpdateMemberRequest request) {
        MemberSummaryDto member = businessService.updateMember(businessId, memberId, request);
        return ResponseEntity.ok(ApiResponse.ok("Member membership updated", member));
    }

    @DeleteMapping("/{businessId}/members/{memberId}")
    public ResponseEntity<ApiResponse<MemberSummaryDto>> removeMember(
            @PathVariable Long businessId,
            @PathVariable Long memberId) {
        MemberSummaryDto member = businessService.removeMember(businessId, memberId);
        return ResponseEntity.ok(ApiResponse.ok("Staff member deactivated from workspace", member));
    }

    @GetMapping("/{businessId}/invitations")
    public ResponseEntity<ApiResponse<List<StaffInvitationDto>>> getInvitations(
            @PathVariable Long businessId) {
        List<StaffInvitationDto> invitations = businessService.getInvitations(businessId);
        return ResponseEntity.ok(ApiResponse.ok("Staff invitations retrieved", invitations));
    }

    @PostMapping("/{businessId}/invitations/{invitationId}/resend")
    public ResponseEntity<ApiResponse<StaffInvitationDto>> resendInvitation(
            @PathVariable Long businessId,
            @PathVariable Long invitationId) {
        StaffInvitationDto invitation = businessService.resendInvitation(businessId, invitationId);
        return ResponseEntity.ok(ApiResponse.ok("Staff invitation refreshed and resent", invitation));
    }

    @DeleteMapping("/{businessId}/invitations/{invitationId}")
    public ResponseEntity<ApiResponse<StaffInvitationDto>> revokeInvitation(
            @PathVariable Long businessId,
            @PathVariable Long invitationId) {
        StaffInvitationDto invitation = businessService.revokeInvitation(businessId, invitationId);
        return ResponseEntity.ok(ApiResponse.ok("Staff invitation revoked", invitation));
    }

    @GetMapping("/{businessId}/settings")
    public ResponseEntity<ApiResponse<BusinessSettingsDto>> getSettings(
            @PathVariable Long businessId) {
        BusinessSettingsDto settings = businessService.getSettings(businessId);
        return ResponseEntity.ok(ApiResponse.ok("Business settings retrieved", settings));
    }

    @PatchMapping("/{businessId}/settings")
    public ResponseEntity<ApiResponse<BusinessSettingsDto>> updateSettings(
            @PathVariable Long businessId,
            @Valid @RequestBody BusinessSettingsDto request) {
        BusinessSettingsDto updated = businessService.updateSettings(businessId, request);
        return ResponseEntity.ok(ApiResponse.ok("Business settings updated", updated));
    }
}
