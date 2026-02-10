package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "Permissions")
@Getter @Setter
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkPermissionId;

    @NotBlank
    @Column(unique = true)
    private String permissionName;

    @CreatedDate()
    private LocalDateTime createdAt;

    @LastModifiedDate()
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();
}
