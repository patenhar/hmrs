package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "Roles")
@Getter @Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkRoleId;

    @NotBlank
    @Column(unique = true)
    private String roleName;

    @CreatedDate()
    private LocalDateTime createdAt;

    @LastModifiedDate()
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "role")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "pk_role_id"),
            inverseJoinColumns = @JoinColumn(name = "pk_permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();
}
