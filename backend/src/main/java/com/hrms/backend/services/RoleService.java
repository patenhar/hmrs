package com.hrms.backend.services;

import com.hrms.backend.dtos.request.RoleInDto;
import com.hrms.backend.dtos.request.RoleReqDto;
import com.hrms.backend.dtos.response.PermissionResDto;
import com.hrms.backend.dtos.response.RoleResDto;
import com.hrms.backend.dtos.spec.RoleSpecDto;
import com.hrms.backend.dtos.spec.SortDto;
import com.hrms.backend.entities.Permission;
import com.hrms.backend.entities.Role;
import com.hrms.backend.repos.RoleRepo;
import com.hrms.backend.utils.JsonStringToSortDto;
import com.hrms.backend.utils.ResourceNotFoundException;
import com.hrms.backend.utils.RoleSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        Role role = new Role();
        role.setRoleName(roleReqDto.getRoleName());
        List<Permission> permissions = roleReqDto.getPermissionIds().stream()
                .map(permId -> modelMapper.map(permissionService.findPermissionById(permId), Permission.class))
                .toList();
        role.setPermissions(permissions);
        return modelMapper.map(roleRepo.save(role), RoleResDto.class);
    }

    public RoleResDto updateRole(UUID id, RoleReqDto roleReqDto) {
        Role role = roleRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        role.setRoleName(roleReqDto.getRoleName());
        List<Permission> permissions = roleReqDto.getPermissionIds().stream()
                .map(permId -> modelMapper.map(permissionService.findPermissionById(permId), Permission.class))
                .toList();
        role.setPermissions(permissions);
        return modelMapper.map(roleRepo.save(role), RoleResDto.class);
    }

    public RoleResDto addPermissionToRole(UUID id, UUID permissionId) {
        RoleResDto role = findRoleById(id);
        role.getPermissions().add(modelMapper.map(permissionService.findPermissionById(permissionId), PermissionResDto.class));
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

    public Page<RoleResDto> searchRolesWithPaginationSortingAndFiltering(RoleInDto dto) {
        RoleSpecDto specDto = RoleSpecDto.builder()
                .roleName(dto.getRoleName())
                .build();

        List<SortDto> sortDtos = JsonStringToSortDto.jsonStringToSortDto(dto.getSort());
        List<Sort.Order> orders = new ArrayList<>();
        if (sortDtos != null) {
            for (SortDto sortDto : sortDtos) {
                Sort.Direction direction = Objects.equals(sortDto.getDirection(), "desc")
                        ? Sort.Direction.DESC : Sort.Direction.ASC;
                orders.add(new Sort.Order(direction, sortDto.getField()));
            }
        }

        PageRequest pageRequest = PageRequest.of(
                dto.getPage(),
                dto.getSize(),
                orders.isEmpty() ? Sort.unsorted() : Sort.by(orders)
        );

        Specification<Role> specification = RoleSpecification.getSpecification(specDto);
        Page<Role> roles = roleRepo.findAll(specification, pageRequest);
        return roles.map(r -> modelMapper.map(r, RoleResDto.class));
    }
}
