package com.hrms.backend.controllers;

import com.hrms.backend.entities.User;
import com.hrms.backend.services.UserService;
import com.hrms.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<User>>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<User>> getById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getById(id));
    }

    @PatchMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<User>> updateRole(@PathVariable UUID id, @RequestBody UUID roleId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateRole(id, roleId));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.delete(id));
    }
}
