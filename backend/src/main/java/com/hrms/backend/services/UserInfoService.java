package com.hrms.backend.services;

import com.hrms.backend.entities.User;
import com.hrms.backend.repos.UserRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import com.hrms.backend.utils.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements UserDetailsService {
    UserRepo userRepo;

    @Autowired
    public UserInfoService(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user =  userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserInfo(user.getPkUserId(), user.getEmail(), user.getPassword(), user.getRole());
    }
}
