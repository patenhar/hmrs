package com.hrms.backend.services.interfaces;

import com.hrms.backend.entities.User;
import com.hrms.backend.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    ApiResponse<List<User>> getAll();

    ApiResponse<User> getById(UUID id);

    ApiResponse<User> updateRole(UUID id, UUID roleId);

    ApiResponse<String> delete(UUID id);
}
