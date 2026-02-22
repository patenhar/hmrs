package com.hrms.backend.services.interfaces;

import com.hrms.backend.dtos.request.RoleReqDto;
import com.hrms.backend.entities.Role;
import com.hrms.backend.utils.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface IRoleService {
    ApiResponse<List<Role>> getAllRoles();

    ApiResponse<Role> getRoleById(UUID id);

    ApiResponse<Role> addRole(RoleReqDto roleReqDto);

    ApiResponse<Role> updateRole(UUID id, RoleReqDto roleReqDto);

    ApiResponse<String> deleteRole(UUID id);
}
