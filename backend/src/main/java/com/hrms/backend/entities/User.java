package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Users")
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkUserId;

    @NotBlank
    @Column(unique = true, nullable = false, updatable = false)
    @Email
    private String email;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String password;

    @CreatedDate()
    private LocalDateTime createdAt;

    @LastModifiedDate()
    private LocalDateTime updatedAt;

    @OneToOne()
    @JoinColumn(name = "fk_role_id", referencedColumnName = "pkRoleId")
    private Role role;
}
