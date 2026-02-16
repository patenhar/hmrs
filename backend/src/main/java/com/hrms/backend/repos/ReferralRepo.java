package com.hrms.backend.repos;

import com.hrms.backend.entities.Referral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReferralRepo extends JpaRepository<Referral, UUID> {
}
