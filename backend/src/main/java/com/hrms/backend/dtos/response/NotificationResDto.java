package com.hrms.backend.dtos.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResDto {
    private UUID pkNotificationId;
    private String title;
    private String description;
    private boolean isRead;
}
