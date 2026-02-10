package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.LoginDto;
import com.hrms.backend.dtos.request.RegisterDto;
import com.hrms.backend.services.AuthService;
import com.hrms.backend.utils.ApiResponse;
import com.something.rest.utils.ApiResponse;
import com.something.rest.utils.CustomException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<String>> register(@RequestBody RegisterDto registerDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.register(registerDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginDto));
    }
}
