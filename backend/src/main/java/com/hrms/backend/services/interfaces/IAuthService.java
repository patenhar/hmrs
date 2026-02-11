package com.hrms.backend.services.interfaces;

import com.hrms.backend.dtos.request.LoginDto;
import com.hrms.backend.dtos.request.RegisterDto;
import com.hrms.backend.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    ResponseEntity<ApiResponse<?>> register(RegisterDto registerDto);
    ResponseEntity<ApiResponse<String>> login(LoginDto loginDto);
}
