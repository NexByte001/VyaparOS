package com.vyparos.vyparos.inventory.repository;

import com.vyparos.vyparos.inventory.entity.StockMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    @Query(value = "SELECT sm FROM StockMovement sm " +
           "JOIN FETCH sm.product p " +
           "LEFT JOIN FETCH sm.recordedByUser u " +
           "WHERE sm.business.id = :businessId " +
           "AND (:productId IS NULL OR sm.product.id = :productId) " +
           "ORDER BY sm.createdAt DESC",
           countQuery = "SELECT COUNT(sm) FROM StockMovement sm " +
           "WHERE sm.business.id = :businessId " +
           "AND (:productId IS NULL OR sm.product.id = :productId)")
    Page<StockMovement> findFilteredStockMovements(
            @Param("businessId") Long businessId,
            @Param("productId") Long productId,
            Pageable pageable
    );

    List<StockMovement> findTop20ByBusinessIdOrderByCreatedAtDesc(Long businessId);
}
