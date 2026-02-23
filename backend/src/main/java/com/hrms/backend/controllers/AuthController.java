package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.AuthReqDto;
import com.hrms.backend.dtos.response.LoginResDto;
import com.hrms.backend.dtos.response.RegisterResDto;
import com.hrms.backend.services.AuthService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResDto>> register(@RequestBody @Validated(OnCreate.class) AuthReqDto authReqDto) {
        RegisterResDto registerResDto = authService.register(authReqDto);
        if (registerResDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("User already registered", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("User registered successfully", registerResDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResDto>> login(@RequestBody @Validated(OnCreate.class) AuthReqDto authReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Login successful", authService.login(authReqDto)));
    }
}
