package com.hrms.backend.services.interfaces;

import com.hrms.backend.entities.User;
import com.hrms.backend.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    ResponseEntity<ApiResponse<List<User>>> getAll();

    ResponseEntity<ApiResponse<?>> getById(UUID id);

    ResponseEntity<ApiResponse<User>> updateRole(UUID id, UUID roleId);

    ResponseEntity<ApiResponse<String>> delete(UUID id);
}
