package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Permissions")
@Getter @Setter
public class Permission extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkPermissionId;

    @NotBlank
    private String permissionName;

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles = new ArrayList<>();
}
