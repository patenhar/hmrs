package com.hrms.backend.services;

import com.hrms.backend.dtos.request.PermissionReqDto;
import com.hrms.backend.dtos.response.PermissionResDto;
import com.hrms.backend.entities.Permission;
import com.hrms.backend.repos.PermissionRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PermissionService {
    private final PermissionRepo permissionRepo;
    private final ModelMapper modelMapper;

    public PermissionService(PermissionRepo permissionRepo, ModelMapper modelMapper) {
        this.permissionRepo = permissionRepo;
        this.modelMapper = modelMapper;
    }

    public PermissionResDto findPermissionById(UUID id) {
        Permission permission = permissionRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense type is not found"));
        return modelMapper.map(permission, PermissionResDto.class);
    }

    public List<PermissionResDto> getAllPermissions() {
        return permissionRepo.findAll().stream().map(st -> modelMapper.map(st, PermissionResDto.class)).toList();
    }

    public PermissionResDto getPermissionById(UUID id) {
        return findPermissionById(id);
    }

    public PermissionResDto addPermission(PermissionReqDto permissionReqDto) {
        Permission permission =  permissionRepo.save(modelMapper.map(permissionReqDto, Permission.class));
        return modelMapper.map(permission, PermissionResDto.class);
    }

    public PermissionResDto updatePermission(UUID id, PermissionReqDto permissionReqDto) {
        PermissionResDto permission = findPermissionById(id);
        modelMapper.map(permissionReqDto, permission);
        Permission updatedPermission =  permissionRepo.save(modelMapper.map(permissionReqDto, Permission.class));
        return modelMapper.map(updatedPermission, PermissionResDto.class);
    }

    public boolean deletePermission(UUID id) {
        findPermissionById(id);
        permissionRepo.deleteById(id);
        return true;
    }
}
