package com.hrms.backend.services;

import com.hrms.backend.dtos.request.AuthDto;
import com.hrms.backend.entities.User;
import com.hrms.backend.repos.UserRepo;
import com.hrms.backend.services.interfaces.IAuthService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.utils.JwtUtil;
import com.hrms.backend.utils.ResourceNotFoundException;
import com.hrms.backend.utils.UserInfo;
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

    public ResponseEntity<ApiResponse<?>> register(AuthDto authDto) {
        userRepo.findByEmail(authDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        authDto.setPassword(passwordEncoder.encode(authDto.getPassword()));
        userRepo.save(modelMapper.map(authDto, User.class));
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("user registered successfully", userRepo.findByEmail(authDto.getEmail())));
    }

    public ResponseEntity<ApiResponse<String>> login(AuthDto authDto) {
        userRepo.findByEmail(authDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authDto.getEmail(),
                        authDto.getPassword()
                )
        );
        String token = jwtUtil.generateToken(auth.getName(), ((UserInfo) auth.getPrincipal()).getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("login successful", token));
    }
}
