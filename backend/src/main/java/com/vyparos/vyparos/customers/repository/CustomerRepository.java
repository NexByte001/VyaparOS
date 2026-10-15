package com.vyparos.vyparos.customers.repository;

import com.vyparos.vyparos.customers.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByIdAndBusinessId(Long id, Long businessId);

    @Query("SELECT c FROM Customer c WHERE c.business.id = :businessId AND c.archived = false " +
           "AND (:query IS NULL OR :query = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR c.phone LIKE CONCAT('%', :query, '%') " +
           "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Customer> searchCustomers(
            @Param("businessId") Long businessId,
            @Param("query") String query,
            Pageable pageable
    );

    long countByBusinessIdAndArchivedFalse(Long businessId);
}
