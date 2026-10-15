package com.vyparos.vyparos.audit.controller;

import com.vyparos.vyparos.audit.dto.AuditLogResponse;
import com.vyparos.vyparos.audit.service.AuditLogService;
import com.vyparos.vyparos.common.dto.ApiResponse;
import com.vyparos.vyparos.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/businesses/{businessId}/admin/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<AuditLogResponse>>> getAuditLogs(
            @PathVariable Long businessId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
            @PageableDefault(size = 30, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<AuditLogResponse> logs = auditLogService.getAuditLogs(businessId, action, entityType, startDate, endDate, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Audit logs retrieved successfully", logs));
    }
}
