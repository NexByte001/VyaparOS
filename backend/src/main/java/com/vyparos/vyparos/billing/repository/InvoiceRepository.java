package com.vyparos.vyparos.billing.repository;

import com.vyparos.vyparos.billing.entity.Invoice;
import com.vyparos.vyparos.billing.entity.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByIdAndBusinessId(Long id, Long businessId);

    @Query(value = "SELECT i FROM Invoice i " +
           "LEFT JOIN FETCH i.customer c " +
           "WHERE i.business.id = :businessId " +
           "AND (:status IS NULL OR i.status = :status) " +
           "AND (:customerId IS NULL OR i.customer.id = :customerId) " +
           "AND (:startDate IS NULL OR i.invoiceDate >= :startDate) " +
           "AND (:endDate IS NULL OR i.invoiceDate <= :endDate)",
           countQuery = "SELECT COUNT(i) FROM Invoice i " +
           "WHERE i.business.id = :businessId " +
           "AND (:status IS NULL OR i.status = :status) " +
           "AND (:customerId IS NULL OR i.customer.id = :customerId) " +
           "AND (:startDate IS NULL OR i.invoiceDate >= :startDate) " +
           "AND (:endDate IS NULL OR i.invoiceDate <= :endDate)")
    Page<Invoice> findFilteredInvoices(
            @Param("businessId") Long businessId,
            @Param("status") InvoiceStatus status,
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    long countByBusinessId(Long businessId);

    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i " +
           "WHERE i.business.id = :businessId " +
           "AND i.status != 'VOID' " +
           "AND (:startDate IS NULL OR i.invoiceDate >= :startDate) " +
           "AND (:endDate IS NULL OR i.invoiceDate <= :endDate)")
    BigDecimal sumInvoicedRevenue(
            @Param("businessId") Long businessId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COALESCE(SUM(i.balanceDue), 0) FROM Invoice i " +
           "WHERE i.business.id = :businessId AND i.status NOT IN ('PAID', 'VOID')")
    BigDecimal sumPendingBalance(@Param("businessId") Long businessId);
}
