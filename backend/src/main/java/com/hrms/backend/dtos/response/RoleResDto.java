package com.hrms.backend.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RoleResDto {
    private UUID pkRoleId;
    private String roleName;
    private List<PermissionResDto> permissions;
}
