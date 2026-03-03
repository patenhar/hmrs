package com.hrms.backend.repos;

import com.hrms.backend.entities.Job;
import com.hrms.backend.entities.JobStakeHolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobStakeHolderRepo extends JpaRepository<JobStakeHolder, UUID> {
    void deleteAllByJob(Job job);
    void deleteAllByUser_PkUserId(UUID userId);
}
