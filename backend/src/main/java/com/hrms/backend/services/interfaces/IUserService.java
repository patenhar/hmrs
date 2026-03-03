package com.hrms.backend.services.interfaces;

import com.hrms.backend.dtos.response.UserResDto;
import com.hrms.backend.entities.User;
import com.hrms.backend.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    ApiResponse<List<UserResDto>> getAll();

    ApiResponse<UserResDto> getById(UUID id);

    ApiResponse<UserResDto> updateRole(UUID id, UUID roleId);

    ApiResponse<String> delete(UUID id);
}
