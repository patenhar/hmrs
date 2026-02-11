package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.RoleDto;
import com.hrms.backend.entities.Role;
import com.hrms.backend.services.RoleService;
import com.hrms.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<Role>>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> getById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.getById(id));
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Role>> add(@RequestBody RoleDto roleDto) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.add(roleDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> updateRole(@PathVariable UUID id, @RequestBody RoleDto roleDto) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.updateRole(id, roleDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.delete(id));
    }
}
