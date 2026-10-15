package com.vyparos.vyparos.business.service;

import com.vyparos.vyparos.business.dto.*;

import java.util.List;

public interface BusinessService {
    BusinessDetailDto createBusiness(Long userId, CreateBusinessRequest request);
    List<BusinessMembershipDto> getUserBusinesses(Long userId);
    BusinessDetailDto getBusinessDetails(Long businessId);
    BusinessDetailDto updateBusiness(Long businessId, UpdateBusinessRequest request);
    List<MemberSummaryDto> getMembers(Long businessId);
    AddMemberResponse addMember(Long businessId, AddMemberRequest request);
    MemberSummaryDto updateMember(Long businessId, Long memberId, UpdateMemberRequest request);
    MemberSummaryDto removeMember(Long businessId, Long memberId);
    List<StaffInvitationDto> getInvitations(Long businessId);
    StaffInvitationDto resendInvitation(Long businessId, Long invitationId);
    StaffInvitationDto revokeInvitation(Long businessId, Long invitationId);
    BusinessSettingsDto getSettings(Long businessId);
    BusinessSettingsDto updateSettings(Long businessId, BusinessSettingsDto request);
}
