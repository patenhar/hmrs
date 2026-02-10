package com.hrms.backend.services;

import com.hrms.backend.dtos.request.LoginDto;
import com.hrms.backend.dtos.request.RegisterDto;
import com.hrms.backend.entities.User;
import com.hrms.backend.repos.AuthRepo;
import com.hrms.backend.repos.UserRepo;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.utils.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    AuthService(UserRepo userRepo, ModelMapper modelMapper, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
    }

    public ApiResponse<String> register(RegisterDto registerDto) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        if(userRepo.findByEmail(registerDto.getEmail())){
            apiResponse.setMessage("user is already registered");
            return apiResponse;
        }
        userRepo.save(modelMapper.map(registerDto, User.class));

        apiResponse.setMessage("user registered successfully");
        return apiResponse;
    }

    public ApiResponse<String> login(LoginDto loginDto) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        if(userRepo.findByEmail(loginDto.getEmail()).isEmpty()){

        }
    }
}
