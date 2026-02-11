package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.PermissionDto;
import com.hrms.backend.entities.Permission;
import com.hrms.backend.services.PermissionService;
import com.hrms.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<Permission>>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Permission>> getById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.getById(id));
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Permission>> add(@RequestBody PermissionDto permissionDto) {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.add(permissionDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Permission>> updatePermission(@PathVariable UUID id, @RequestBody PermissionDto permissionDto) {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.updatePermission(id, permissionDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.delete(id));
    }
}
