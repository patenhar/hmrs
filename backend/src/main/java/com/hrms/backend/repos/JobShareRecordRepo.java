package com.hrms.backend.repos;

import com.hrms.backend.entities.JobShareRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobShareRecordRepo extends JpaRepository<JobShareRecord, UUID> {
}
