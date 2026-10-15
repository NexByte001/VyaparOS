package com.vyparos.vyparos.services.repository;

import com.vyparos.vyparos.services.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    List<ServiceEntity> findAllByBusinessIdAndActiveTrue(Long businessId);
    List<ServiceEntity> findAllByBusinessId(Long businessId);
    Optional<ServiceEntity> findByIdAndBusinessId(Long id, Long businessId);
}
