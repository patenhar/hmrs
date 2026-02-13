package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
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
    @NotBlank(message = "Role name is required", groups = {OnCreate.class, OnUpdate.class})
    private String roleName;

    private List<UUID> permissionIds = new ArrayList<>();
}
