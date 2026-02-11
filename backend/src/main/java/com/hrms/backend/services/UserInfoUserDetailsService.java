package com.hrms.backend.services;

import com.hrms.backend.entities.User;
import com.hrms.backend.repos.UserRepo;
import com.hrms.backend.utils.UserInfoUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserInfoUserDetailsService implements UserDetailsService {
    UserRepo userRepo;

    @Autowired
    public UserInfoUserDetailsService(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user =  userRepo.findByEmail(email);
        return new UserInfoUserDetails(user);
    }
}
