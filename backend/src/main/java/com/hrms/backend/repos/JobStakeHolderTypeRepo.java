package com.hrms.backend.repos;

import com.hrms.backend.entities.JobStakeHolderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobStakeHolderTypeRepo extends JpaRepository<JobStakeHolderType, UUID> {
    List<JobStakeHolderType> findJobStakeHolderTypesByJobStakeHolderTypeNameContainingIgnoreCase(String name);
}
