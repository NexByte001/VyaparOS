package com.vyparos.vyparos.business.repository;

import com.vyparos.vyparos.business.entity.InvitationStatus;
import com.vyparos.vyparos.business.entity.StaffInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffInvitationRepository extends JpaRepository<StaffInvitation, Long> {

    Optional<StaffInvitation> findByInvitationToken(String invitationToken);

    List<StaffInvitation> findAllByBusinessIdOrderByCreatedAtDesc(Long businessId);

    Optional<StaffInvitation> findByBusinessIdAndEmailIgnoreCaseAndStatus(
            Long businessId, String email, InvitationStatus status);

    boolean existsByBusinessIdAndEmailIgnoreCaseAndStatus(
            Long businessId, String email, InvitationStatus status);

    long countByBusinessIdAndStatus(Long businessId, InvitationStatus status);
}
