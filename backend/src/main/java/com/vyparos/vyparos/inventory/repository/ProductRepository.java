package com.vyparos.vyparos.inventory.repository;

import com.vyparos.vyparos.inventory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByIdAndBusinessId(Long id, Long businessId);

    @Query(value = "SELECT p FROM Product p " +
           "LEFT JOIN FETCH p.supplier s " +
           "WHERE p.business.id = :businessId " +
           "AND (:activeOnly = false OR p.active = true) " +
           "AND (:lowStockOnly = false OR p.currentStock <= p.reorderThreshold) " +
           "AND (:query IS NULL OR :query = '' " +
           "OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(p.category) LIKE LOWER(CONCAT('%', :query, '%')))",
           countQuery = "SELECT COUNT(p) FROM Product p " +
           "WHERE p.business.id = :businessId " +
           "AND (:activeOnly = false OR p.active = true) " +
           "AND (:lowStockOnly = false OR p.currentStock <= p.reorderThreshold) " +
           "AND (:query IS NULL OR :query = '' " +
           "OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(p.category) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Product> searchProducts(
            @Param("businessId") Long businessId,
            @Param("query") String query,
            @Param("activeOnly") boolean activeOnly,
            @Param("lowStockOnly") boolean lowStockOnly,
            Pageable pageable
    );

    @Query("SELECT p FROM Product p WHERE p.business.id = :businessId AND p.active = true AND p.currentStock <= p.reorderThreshold")
    List<Product> findLowStockProducts(@Param("businessId") Long businessId);

    long countByBusinessIdAndActiveTrueAndCurrentStockLessThanEqual(Long businessId, Integer threshold);

    Optional<Product> findByBusinessIdAndSkuIgnoreCase(Long businessId, String sku);

    Optional<Product> findByBusinessIdAndNameIgnoreCase(Long businessId, String name);

    boolean existsByBusinessIdAndSkuIgnoreCaseAndIdNot(Long businessId, String sku, Long id);

    boolean existsByBusinessIdAndNameIgnoreCaseAndIdNot(Long businessId, String name, Long id);
}
