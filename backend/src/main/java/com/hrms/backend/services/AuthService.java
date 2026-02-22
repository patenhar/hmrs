package com.hrms.backend.services;

import com.hrms.backend.dtos.request.AuthReqDto;
import com.hrms.backend.dtos.response.LoginResDto;
import com.hrms.backend.entities.User;
import com.hrms.backend.repos.UserRepo;
import com.hrms.backend.services.interfaces.IAuthService;
import com.hrms.backend.utils.ResourceNotFoundException;
import com.hrms.backend.utils.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AuthService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepo userRepo, ModelMapper modelMapper, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean register(AuthReqDto authReqDto) {
        if (userRepo.findByEmail(authReqDto.getEmail()).isPresent()) {
            return false;
        }
        authReqDto.setPassword(passwordEncoder.encode(authReqDto.getPassword()));
        userRepo.save(modelMapper.map(authReqDto, User.class));
        return true;
    }

    public LoginResDto login(AuthReqDto authReqDto) {
        userRepo.findByEmail(authReqDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authReqDto.getEmail(),
                        authReqDto.getPassword()
                )
        );
        String token = jwtService.generateToken(auth.getName(), ((UserInfo) auth.getPrincipal()).getUserId());
        return new LoginResDto(token);
    }
}
