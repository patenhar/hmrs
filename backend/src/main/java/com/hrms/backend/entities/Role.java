package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Roles")
@Getter @Setter
public class Role extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkRoleId;

    @NotBlank
    private String roleName;

    @OneToMany(mappedBy = "role")
    private List<User> users;

    @ManyToMany
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "fk_role_id"),
            inverseJoinColumns = @JoinColumn(name = "fk_permission_id")
    )
    private List<Permission> permissions = new ArrayList<>();
}
