package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Notifications")
@Getter @Setter
public class Notification extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkNotificationId;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private boolean isRead;

    @ManyToOne
    @JoinColumn(name = "fk_user_Id", referencedColumnName = "pkUserId")
    private User user;
}
