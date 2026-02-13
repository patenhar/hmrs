package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.PermissionDto;
import com.hrms.backend.entities.Permission;
import com.hrms.backend.services.PermissionService;
import com.hrms.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasAuthority;

@RestController
@RequestMapping("api/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_PERMISSION')")
    public ResponseEntity<ApiResponse<List<Permission>>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.getAll());
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_PERMISSION')")
    public ResponseEntity<ApiResponse<Permission>> getById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.getById(id));
    }

    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_PERMISSION')")
    public ResponseEntity<ApiResponse<Permission>> add(@RequestBody PermissionDto permissionDto) {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.add(permissionDto));
    }

    @PatchMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_PERMISSION')")
    public ResponseEntity<ApiResponse<Permission>> updatePermission(@PathVariable UUID id, @RequestBody PermissionDto permissionDto) {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.updatePermission(id, permissionDto));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_PERMISSION')")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.delete(id));
    }
}
