package com.hrms.backend.services;

import com.hrms.backend.dtos.request.PermissionDto;
import com.hrms.backend.entities.Permission;
import com.hrms.backend.repos.PermissionRepo;
import com.hrms.backend.services.interfaces.IPermissionService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class PermissionService implements IPermissionService {
    private final PermissionRepo permissionRepo;
    private final ModelMapper modelMapper;

    public PermissionService(PermissionRepo permissionRepo, ModelMapper modelMapper) {
        this.permissionRepo = permissionRepo;
        this.modelMapper = modelMapper;
    }

    private Permission findById(UUID id) {
        return permissionRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid permission id"));
    }

    @Override
    public ApiResponse<List<Permission>> getAll() {
        return new ApiResponse<>("Fetched all permissions", permissionRepo.findAll());
    }

    @Override
    public ApiResponse<Permission> getById(UUID id) {
        return new ApiResponse<>("Fetched permission", findById(id));
    }

    @Override
    public ApiResponse<Permission> add(PermissionDto permissionDto) {
        Permission permission = permissionRepo.save(modelMapper.map(permissionDto, Permission.class));
        return new ApiResponse<>("Permission added successfully", permission);
    }

    @Override
    public ApiResponse<Permission> updatePermission(UUID id, PermissionDto permissionDto) {
        Permission permission = findById(id);
        modelMapper.map(permissionDto, permission);
        permissionRepo.save(permission);
        return new ApiResponse<>("Successfully updated the permission", findById(id));
    }

    @Override
    public ApiResponse<String> delete(UUID id) {
        findById(id);
        permissionRepo.deleteById(id);
        return new ApiResponse<>("Permission deleted successfully", null);
    }
}
