package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.PermissionReqDto;
import com.hrms.backend.dtos.response.PermissionResDto;
import com.hrms.backend.entities.Permission;
import com.hrms.backend.services.PermissionService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<PermissionResDto>>> getAllPermissions() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All permissions fetched successfully", permissionService.getAllPermissions()));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<PermissionResDto>> getPermissionById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Permission fetched successfully", permissionService.getPermissionById(id)));
    }

    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_ROLE')")
    public ResponseEntity<ApiResponse<PermissionResDto>> addPermission(@RequestBody @Validated(OnCreate.class) PermissionReqDto permissionReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Permission added successfully", permissionService.addPermission(permissionReqDto)));
    }

    @PatchMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<PermissionResDto>> updatePermission(@PathVariable UUID id, @RequestBody @Validated(OnUpdate.class) PermissionReqDto permissionReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Permission updated successfully", permissionService.updatePermission(id, permissionReqDto)));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<String>> deletePermission(@PathVariable UUID id) {
        String res = "Permission could not be deleted";
        if (permissionService.deletePermission(id)){
            res = "Permission deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null ));
    }
}
