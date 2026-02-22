package com.hrms.backend.repos;

import com.hrms.backend.entities.GameBookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameBookingStatusRepo extends JpaRepository<GameBookingStatus, UUID> {
}
