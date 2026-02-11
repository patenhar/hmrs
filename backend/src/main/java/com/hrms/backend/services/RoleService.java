package com.hrms.backend.services;

import com.hrms.backend.dtos.request.RoleDto;
import com.hrms.backend.entities.Permission;
import com.hrms.backend.entities.Role;
import com.hrms.backend.repos.PermissionRepo;
import com.hrms.backend.repos.RoleRepo;
import com.hrms.backend.services.interfaces.IRoleService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleService implements IRoleService {
    private final RoleRepo roleRepo;
    private final PermissionRepo permissionRepo;
    private final ModelMapper modelMapper;


    public RoleService(RoleRepo roleRepo, PermissionRepo permissionRepo, ModelMapper modelMapper) {
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
        this.modelMapper = modelMapper;
    }

    private Role findById(UUID id) {
        return roleRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid role id"));
    }

    @Override
    public ApiResponse<List<Role>> getAll() {
        return new ApiResponse<>("all roles fetched successfully", roleRepo.findAll());
    }

    @Override
    public ApiResponse<Role> getById(UUID id) {
        return new ApiResponse<>("role fetched successfully", findById(id));
    }

    @Override
    public ApiResponse<Role> add(RoleDto roleDto) {
        List<Permission> permissions = roleDto.getPermissionIds()
                                                .stream()
                                                .map(i -> permissionRepo.findById(i).orElseThrow(() -> new ResourceNotFoundException("invalid permission id")))
                                                .toList();
        Role role = new Role();
        role.setRoleName(roleDto.getRoleName());
        role.setPermissions(permissions);
        roleRepo.save(role);
        return new ApiResponse<>("role added successfully", role);
    }

    @Override
    public ApiResponse<Role> updateRole(UUID id, RoleDto roleDto) {
        Role role = findById(id);
        List<Permission> permissions = roleDto.getPermissionIds()
                .stream()
                .map(i -> permissionRepo.findById(i).orElseThrow(() -> new ResourceNotFoundException("invalid permission id")))
                .toList();
        role.setRoleName(roleDto.getRoleName());
        role.setPermissions(permissions);
        roleRepo.save(role);
        return new ApiResponse<>("role updated successfully", role);
    }

    @Override
    public ApiResponse<String> delete(UUID id) {
        findById(id);
        roleRepo.deleteById(id);
        return new ApiResponse<>("role deleted successfully", null);
    }
}
