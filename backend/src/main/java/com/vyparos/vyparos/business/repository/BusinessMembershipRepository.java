package com.vyparos.vyparos.business.repository;

import com.vyparos.vyparos.business.entity.BusinessMembership;
import com.vyparos.vyparos.business.entity.BusinessRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessMembershipRepository extends JpaRepository<BusinessMembership, Long> {

    @Query("SELECT bm FROM BusinessMembership bm JOIN FETCH bm.business WHERE bm.user.id = :userId AND bm.active = true")
    List<BusinessMembership> findAllByUserIdAndActiveTrue(@Param("userId") Long userId);

    @Query("SELECT bm FROM BusinessMembership bm JOIN FETCH bm.user WHERE bm.business.id = :businessId")
    List<BusinessMembership> findAllByBusinessId(@Param("businessId") Long businessId);

    Optional<BusinessMembership> findByBusinessIdAndUserId(Long businessId, Long userId);

    Optional<BusinessMembership> findByBusinessIdAndUserIdAndActiveTrue(Long businessId, Long userId);

    long countByBusinessIdAndRoleAndActiveTrue(Long businessId, BusinessRole role);
}
