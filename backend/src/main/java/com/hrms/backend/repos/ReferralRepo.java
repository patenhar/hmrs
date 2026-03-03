package com.hrms.backend.repos;

import com.hrms.backend.entities.Referral;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ReferralRepo extends JpaRepository<Referral, UUID> {
    @Modifying
    @Query("UPDATE Referral r SET r.user = null WHERE r.user.pkUserId = :userId")
    void clearUserByUserId(@Param("userId") UUID userId);

    Page<Referral> findAll(Specification<Referral> specification, Pageable pageable);
}
