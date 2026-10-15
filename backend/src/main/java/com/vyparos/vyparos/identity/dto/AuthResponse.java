package com.vyparos.vyparos.identity.dto;

import com.vyparos.vyparos.business.dto.BusinessMembershipDto;

import java.util.List;

public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private long expiresInMs;
    private UserSummaryDto user;
    private List<BusinessMembershipDto> memberships;

    public AuthResponse() {}

    public AuthResponse(String token, long expiresInMs, UserSummaryDto user, List<BusinessMembershipDto> memberships) {
        this.token = token;
        this.tokenType = "Bearer";
        this.expiresInMs = expiresInMs;
        this.user = user;
        this.memberships = memberships;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public long getExpiresInMs() {
        return expiresInMs;
    }

    public void setExpiresInMs(long expiresInMs) {
        this.expiresInMs = expiresInMs;
    }

    public UserSummaryDto getUser() {
        return user;
    }

    public void setUser(UserSummaryDto user) {
        this.user = user;
    }

    public List<BusinessMembershipDto> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<BusinessMembershipDto> memberships) {
        this.memberships = memberships;
    }
}
