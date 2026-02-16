package com.hrms.backend.repos;

import com.hrms.backend.entities.ReferralStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReferralStatusRepo extends JpaRepository<ReferralStatus, UUID> {
}
