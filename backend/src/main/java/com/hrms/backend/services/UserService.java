package com.hrms.backend.services;

import com.hrms.backend.entities.User;
import com.hrms.backend.repos.RoleRepo;
import com.hrms.backend.repos.UserRepo;
import com.hrms.backend.services.interfaces.IUserService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.utils.ResourceNotFoundException;
import com.hrms.backend.utils.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements IUserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    public User findUserById(UUID id) {
        return userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) auth.getPrincipal();
        return findUserById(userInfo.getUserId());
    }

    private User findById(UUID id) {
        return userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public ApiResponse<List<User>> getAll() {
        return new ApiResponse<>("Users fetched successfully", userRepo.findAll());
    }

    @Override
    public ApiResponse<User> getById(UUID id) {
        return new ApiResponse<>("User fetched successfully", findById(id));
    }

    @Override
    public ApiResponse<User> updateRole(UUID id, UUID roleId) {
        User user = findById(id);
        user.setRole(roleRepo.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("invalid role id")));
        userRepo.save(user);
        return new ApiResponse<>("User role updated successfully", user);
    }

    @Override
    public ApiResponse<String> delete(UUID id) {
        findById(id);
        userRepo.deleteById(id);
        return new ApiResponse<>("User deleted successfully", null);
    }
}
