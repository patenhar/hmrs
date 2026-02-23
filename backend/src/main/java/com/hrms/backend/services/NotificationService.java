package com.hrms.backend.services;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.hrms.backend.dtos.response.NotificationResDto;
import com.hrms.backend.entities.Notification;
import com.hrms.backend.repos.NotificationRepo;

@Service
public class NotificationService {
    private final NotificationRepo notificationRepo;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public NotificationService(NotificationRepo notificationRepo, UserService userService, ModelMapper modelMapper) {
        this.notificationRepo = notificationRepo;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public void createNotification(String title, String description, UUID userId) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setDescription(description);
        notification.setUser(userService.findById(userId));
        notification.setRead(false);
        notificationRepo.save(notification);
    }

    public List<NotificationResDto> getNotificationsForUser() {
        return notificationRepo.findAllNotificationsByUserPkUserIdOrderByCreatedAtDesc(userService.getAuthenticatedUser().getPkUserId()).stream().map(n -> modelMapper.map(n, NotificationResDto.class)).toList();
    }

    public void markAsRead(UUID notificationId) {
        Notification notification = notificationRepo.findById(notificationId).orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepo.save(notification);
    }
}
