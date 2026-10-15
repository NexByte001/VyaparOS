package com.vyparos.vyparos.audit.repository;

import com.vyparos.vyparos.audit.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    @Query(value = "SELECT a FROM AuditLog a " +
           "LEFT JOIN FETCH a.user u " +
           "WHERE a.business.id = :businessId " +
           "AND (:action IS NULL OR LOWER(a.action) LIKE LOWER(CONCAT('%', :action, '%'))) " +
           "AND (:entityType IS NULL OR LOWER(a.entityType) = LOWER(:entityType)) " +
           "AND (:startDate IS NULL OR a.timestamp >= :startDate) " +
           "AND (:endDate IS NULL OR a.timestamp <= :endDate) " +
           "ORDER BY a.timestamp DESC",
           countQuery = "SELECT COUNT(a) FROM AuditLog a " +
           "WHERE a.business.id = :businessId " +
           "AND (:action IS NULL OR LOWER(a.action) LIKE LOWER(CONCAT('%', :action, '%'))) " +
           "AND (:entityType IS NULL OR LOWER(a.entityType) = LOWER(:entityType)) " +
           "AND (:startDate IS NULL OR a.timestamp >= :startDate) " +
           "AND (:endDate IS NULL OR a.timestamp <= :endDate)")
    Page<AuditLog> findFilteredAuditLogs(
            @Param("businessId") Long businessId,
            @Param("action") String action,
            @Param("entityType") String entityType,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            Pageable pageable
    );
}
