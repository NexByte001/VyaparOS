package com.vyparos.vyparos.inventory.repository;

import com.vyparos.vyparos.inventory.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findAllByBusinessIdAndActiveTrue(Long businessId);
    List<Supplier> findAllByBusinessId(Long businessId);
    Optional<Supplier> findByIdAndBusinessId(Long id, Long businessId);
}
