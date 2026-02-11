package com.hrms.backend.services;

import com.hrms.backend.dtos.request.LoginDto;
import com.hrms.backend.dtos.request.RegisterDto;
import com.hrms.backend.entities.User;
import com.hrms.backend.repos.UserRepo;
import com.hrms.backend.services.interfaces.IAuthService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.utils.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService implements IAuthService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepo userRepo, ModelMapper modelMapper, JwtUtil jwtUtil, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<ApiResponse<?>> register(RegisterDto registerDto) {
        if(userRepo.findByEmail(registerDto.getEmail()) != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("user is already registered", null));
        }
        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userRepo.save(modelMapper.map(registerDto, User.class));
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("user registered successfully", userRepo.findByEmail(registerDto.getEmail())));
    }

    public ResponseEntity<ApiResponse<String>> login(LoginDto loginDto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );

        String token = jwtUtil.generateToken(auth.getName());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("login successful", token));
    }
}
