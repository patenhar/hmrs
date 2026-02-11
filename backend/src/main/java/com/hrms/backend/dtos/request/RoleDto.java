package com.hrms.backend.dtos.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class RoleDto {
    @NotBlank
    @Column(unique = true)
    private String roleName;

    @NotBlank
    private List<UUID> permissionIds = new ArrayList<>();
}
