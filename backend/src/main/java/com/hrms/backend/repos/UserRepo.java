package com.hrms.backend.repos;

import com.hrms.backend.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;


public interface UserRepo extends JpaRepository<User, UUID>{
    Optional<User> findByEmail(String email);
}
