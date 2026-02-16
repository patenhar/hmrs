package com.hrms.backend.repos;

import com.hrms.backend.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobRepo extends JpaRepository<Job, UUID> {
}
