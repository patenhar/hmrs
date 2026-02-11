package com.hrms.backend.services;

import com.hrms.backend.entities.User;
import com.hrms.backend.repos.RoleRepo;
import com.hrms.backend.repos.UserRepo;
import com.hrms.backend.services.interfaces.IUserService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    private User findById(UUID id) {
        return userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public ResponseEntity<ApiResponse<List<User>>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Users fetched successfully", userRepo.findAll()));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> getById(UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("User fetched successfully", findById(id)));
    }

    @Override
    public ResponseEntity<ApiResponse<User>> updateRole(UUID id, UUID roleId) {
        User user = findById(id);
        user.setRole(roleRepo.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("invalid role id")));
        userRepo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("User role updated successfully", user));
    }

    @Override
    public ResponseEntity<ApiResponse<String>> delete(UUID id) {
        findById(id);
        userRepo.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("User deleted successfully", null));
    }
}
