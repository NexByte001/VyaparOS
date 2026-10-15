package com.vyparos.vyparos.business.repository;

import com.vyparos.vyparos.business.entity.BusinessSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessSettingsRepository extends JpaRepository<BusinessSettings, Long> {
    Optional<BusinessSettings> findByBusinessId(Long businessId);
}
