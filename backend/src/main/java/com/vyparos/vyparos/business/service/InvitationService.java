package com.vyparos.vyparos.business.service;

import com.vyparos.vyparos.business.dto.MemberSummaryDto;
import com.vyparos.vyparos.business.dto.RegisterAndAcceptInviteRequest;
import com.vyparos.vyparos.business.dto.StaffInvitationDto;
import com.vyparos.vyparos.identity.dto.AuthResponse;

public interface InvitationService {
    StaffInvitationDto getInvitation(String token);
    AuthResponse registerAndAccept(String token, RegisterAndAcceptInviteRequest request);
    MemberSummaryDto acceptAuthenticated(String token, Long userId);
}
