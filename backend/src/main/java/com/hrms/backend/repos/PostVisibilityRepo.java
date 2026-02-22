package com.hrms.backend.repos;

import com.hrms.backend.entities.PostVisibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostVisibilityRepo extends JpaRepository<PostVisibility, UUID> {
    Optional<PostVisibility> findByVisibilityIgnoreCase(String visibility);
}
