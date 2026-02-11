package com.hrms.backend.services.interfaces;

import com.hrms.backend.dtos.request.PermissionDto;
import com.hrms.backend.entities.Permission;
import com.hrms.backend.utils.ApiResponse;
import java.util.List;
import java.util.UUID;

public interface IPermissionService {
    ApiResponse<List<Permission>> getAll();

    ApiResponse<Permission> getById(UUID id);

    ApiResponse<Permission> add(PermissionDto permissionDto);

    ApiResponse<Permission> updatePermission(UUID id, PermissionDto permissionDto);

    ApiResponse<String> delete(UUID id);
}
