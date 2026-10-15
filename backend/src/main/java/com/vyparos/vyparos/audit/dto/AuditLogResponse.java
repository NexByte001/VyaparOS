package com.vyparos.vyparos.audit.dto;

import com.vyparos.vyparos.audit.entity.AuditLog;
import com.vyparos.vyparos.identity.dto.UserSummaryDto;

import java.time.Instant;

public class AuditLogResponse {

    private Long id;
    private Long businessId;
    private UserSummaryDto user;
    private String action;
    private String entityType;
    private String entityId;
    private String details;
    private String reason;
    private Instant timestamp;

    public AuditLogResponse() {}

    public static AuditLogResponse fromEntity(AuditLog log) {
        AuditLogResponse response = new AuditLogResponse();
        response.setId(log.getId());
        response.setBusinessId(log.getBusiness().getId());
        if (log.getUser() != null) {
            response.setUser(UserSummaryDto.fromEntity(log.getUser()));
        }
        response.setAction(log.getAction());
        response.setEntityType(log.getEntityType());
        response.setEntityId(log.getEntityId());
        response.setDetails(log.getDetails());
        response.setReason(log.getReason());
        response.setTimestamp(log.getTimestamp());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public UserSummaryDto getUser() {
        return user;
    }

    public void setUser(UserSummaryDto user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
