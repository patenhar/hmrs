package com.hrms.backend.repos;

import com.hrms.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;


public interface UserRepo extends JpaRepository<User, UUID>{
    User findByEmail(String email);
}
