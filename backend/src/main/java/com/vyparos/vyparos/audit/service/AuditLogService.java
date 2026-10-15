package com.vyparos.vyparos.audit.service;

import com.vyparos.vyparos.audit.dto.AuditLogResponse;
import com.vyparos.vyparos.business.entity.Business;
import com.vyparos.vyparos.common.dto.PageResponse;
import com.vyparos.vyparos.identity.entity.User;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface AuditLogService {
    void logAction(Business business, User user, String action, String entityType, String entityId, String details, String reason);
    PageResponse<AuditLogResponse> getAuditLogs(Long businessId, String action, String entityType, Instant startDate, Instant endDate, Pageable pageable);
}
