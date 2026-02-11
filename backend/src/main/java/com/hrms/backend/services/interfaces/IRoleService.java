package com.hrms.backend.services.interfaces;

import com.hrms.backend.dtos.request.RoleDto;
import com.hrms.backend.entities.Role;
import com.hrms.backend.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface IRoleService {
    ApiResponse<List<Role>> getAll();

    ApiResponse<Role> getById(UUID id);

    ApiResponse<Role> add(RoleDto roleDto);

    ApiResponse<Role> updateRole(UUID id, RoleDto roleDto);

    ApiResponse<String> delete(UUID id);
}
