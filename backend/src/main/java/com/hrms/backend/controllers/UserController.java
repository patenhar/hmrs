package com.hrms.backend.controllers;

import com.hrms.backend.entities.User;
import com.hrms.backend.services.UserService;
import com.hrms.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ApiResponse<List<User>>> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable UUID id) {
        return userService.getById(id);
    }

    @PreAuthorize("hasRole('ROLE_HR')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateRole(@PathVariable UUID id, @RequestBody UUID roleId) {
        return userService.updateRole(id, roleId);
    }

    @PreAuthorize("hasRole('ROLE_HR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        return userService.delete(id);
    }


}
