package com.hrms.backend.services;

import com.hrms.backend.dtos.request.RoleReqDto;
import com.hrms.backend.dtos.response.RoleResDto;
import com.hrms.backend.entities.Permission;
import com.hrms.backend.entities.Role;
import com.hrms.backend.repos.RoleRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleService {
    private final RoleRepo roleRepo;
    private final ModelMapper modelMapper;
    private final PermissionService permissionService;

    public RoleService(RoleRepo roleRepo, ModelMapper modelMapper, PermissionService permissionService) {
        this.roleRepo = roleRepo;
        this.modelMapper = modelMapper;
        this.permissionService = permissionService;
    }

    public RoleResDto findRoleById(UUID id) {
        Role role = roleRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense type is not found"));
        return modelMapper.map(role, RoleResDto.class);
    }

    public List<RoleResDto> getAllRoles() {
        return roleRepo.findAll().stream().map(st -> modelMapper.map(st, RoleResDto.class)).toList();
    }

    public List<RoleResDto> getRoleByName(String roleName) {
        return roleRepo.findAllRolesByRoleNameContainingIgnoreCase(roleName).stream().map(st -> modelMapper.map(st, RoleResDto.class)).toList();
    }

    public RoleResDto getRoleById(UUID id) {
        return findRoleById(id);
    }

    public RoleResDto addRole(RoleReqDto roleReqDto) {
        Role role =  roleRepo.save(modelMapper.map(roleReqDto, Role.class));
        return modelMapper.map(role, RoleResDto.class);
    }

    public RoleResDto updateRole(UUID id, RoleReqDto roleReqDto) {
        RoleResDto role = findRoleById(id);
        modelMapper.map(roleReqDto, role);
        Role updatedRole =  roleRepo.save(modelMapper.map(roleReqDto, Role.class));
        return modelMapper.map(updatedRole, RoleResDto.class);
    }

    public RoleResDto addPermissionToRole(UUID id, UUID permissionId) {
        RoleResDto role = findRoleById(id);
        role.getPermissions().add(modelMapper.map(permissionService.findPermissionById(permissionId), Permission.class));
        Role updatedRole =  roleRepo.save(modelMapper.map(role, Role.class));
        return modelMapper.map(updatedRole, RoleResDto.class);
    }
    public boolean deleteRole(UUID id) {
        findRoleById(id);
        roleRepo.deleteById(id);
        return true;
    }

    public RoleResDto getDefaultRole() {
        Role role = roleRepo.findFirstByRoleName("Employee").orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
        return modelMapper.map(role, RoleResDto.class);
    }
}
