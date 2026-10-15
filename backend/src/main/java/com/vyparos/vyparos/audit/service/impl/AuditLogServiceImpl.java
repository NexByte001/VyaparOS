package com.vyparos.vyparos.audit.service.impl;

import com.vyparos.vyparos.audit.dto.AuditLogResponse;
import com.vyparos.vyparos.audit.entity.AuditLog;
import com.vyparos.vyparos.audit.repository.AuditLogRepository;
import com.vyparos.vyparos.audit.service.AuditLogService;
import com.vyparos.vyparos.business.entity.Business;
import com.vyparos.vyparos.common.dto.PageResponse;
import com.vyparos.vyparos.identity.entity.User;
import com.vyparos.vyparos.security.TenantSecurityGuard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final TenantSecurityGuard securityGuard;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository, TenantSecurityGuard securityGuard) {
        this.auditLogRepository = auditLogRepository;
        this.securityGuard = securityGuard;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void logAction(Business business, User user, String action, String entityType, String entityId, String details, String reason) {
        AuditLog log = new AuditLog(business, user, action, entityType, entityId, details, reason);
        auditLogRepository.save(log);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AuditLogResponse> getAuditLogs(
            Long businessId, String action, String entityType, Instant startDate, Instant endDate, Pageable pageable) {
        securityGuard.requireAdminOrOwner(businessId);
        Page<AuditLog> page = auditLogRepository.findFilteredAuditLogs(businessId, action, entityType, startDate, endDate, pageable);
        return PageResponse.from(page.map(AuditLogResponse::fromEntity));
    }
}
