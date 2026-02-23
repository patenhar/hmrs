package com.hrms.backend.repos;

import com.hrms.backend.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepo extends JpaRepository<Notification, UUID> {
    List<Notification> findAllNotificationsByUserPkUserIdOrderByCreatedAtDesc(UUID userId);
    
}
