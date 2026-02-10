package com.hrms.backend.services.Interfaces;

import com.hrms.backend.dtos.request.LoginDto;
import com.hrms.backend.dtos.request.RegisterDto;
import com.hrms.backend.utils.ApiResponse;

public interface IAuthService {
    ApiResponse<String> register(RegisterDto registerDto);
    ApiResponse<String> login(LoginDto loginDto);
}
