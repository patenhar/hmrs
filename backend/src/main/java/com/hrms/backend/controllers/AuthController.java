package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.AuthDto;
import com.hrms.backend.services.AuthService;
import com.hrms.backend.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody AuthDto authDto) {
        return authService.register(authDto);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody AuthDto authDto) {
        return authService.login(authDto);
    }
}
