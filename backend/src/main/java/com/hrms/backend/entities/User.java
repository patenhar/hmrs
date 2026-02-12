package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "Users")
@Getter @Setter
public class User extends AuditableTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkUserId;

    @NotBlank
    @Column(unique = true, nullable = false, updatable = false)
    @Email
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @ManyToOne()
    @JoinColumn(name = "fk_role_id", referencedColumnName = "pkRoleId")
    private Role role;
}
