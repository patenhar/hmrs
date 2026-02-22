package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class RoleReqDto {
    @NotBlank(message = "Role name is required", groups = {OnCreate.class, OnUpdate.class})
    private String roleName;

    @NotNull(message = "At least one permission is required", groups = {OnCreate.class, OnUpdate.class})
    private List<@NotNull(message = "Permission id cannot be empty") UUID> permissionIds = new ArrayList<>();
}
