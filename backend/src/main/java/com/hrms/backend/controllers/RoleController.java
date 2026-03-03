package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.RoleInDto;
import com.hrms.backend.dtos.request.RoleReqDto;
import com.hrms.backend.dtos.response.RoleResDto;
import com.hrms.backend.entities.Role;
import com.hrms.backend.services.RoleService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/roles")
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('MANAGE_ALL_USER')")
    public ResponseEntity<ApiResponse<List<RoleResDto>>> getAllRoles() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All roles fetched successfully", roleService.getAllRoles()));
    }

    @GetMapping("/filtering&pagination&sorting")
    @PreAuthorize("hasAuthority('MANAGE_ALL_USER')")
    public ResponseEntity<Page<RoleResDto>> getRolesPaginated(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String sort,
            @RequestParam(name = "role-name", required = false) String roleName
    ) {
        RoleInDto dto = RoleInDto.builder()
                .page(page)
                .size(size)
                .sort(sort)
                .roleName(roleName)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(
                roleService.searchRolesWithPaginationSortingAndFiltering(dto)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_ALL_USER')")
    public ResponseEntity<ApiResponse<RoleResDto>> getRoleById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Role fetched successfully", roleService.getRoleById(id)));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('MANAGE_ALL_USER')")
    public ResponseEntity<ApiResponse<List<RoleResDto>>> getRoleByName(@RequestParam String roleName) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Roles fetched successfully", roleService.getRoleByName(roleName)));
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('MANAGE_ALL_USER')")
    public ResponseEntity<ApiResponse<RoleResDto>> addRole(@RequestBody @Validated(OnCreate.class) RoleReqDto roleReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Role added successfully", roleService.addRole(roleReqDto)));
    }

    @PutMapping("/{id}/add/permissions/{permId}")
    @PreAuthorize("hasAuthority('MANAGE_ALL_USER')")
    public ResponseEntity<ApiResponse<RoleResDto>> addPermissionToRole(@PathVariable UUID id, @PathVariable UUID permId) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Role updated successfully", roleService.addPermissionToRole(id, permId)));
    }
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_ALL_USER')")
    public ResponseEntity<ApiResponse<RoleResDto>> updateRole(@PathVariable UUID id, @RequestBody @Validated(OnUpdate.class) RoleReqDto roleReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Role updated successfully", roleService.updateRole(id, roleReqDto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_ALL_USER')")
    public ResponseEntity<ApiResponse<String>> deleteRole(@PathVariable UUID id) {
        String res = "Role could not be deleted";
        if (roleService.deleteRole(id)){
            res = "Role deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null ));
    }
}
