package com.hrms.backend.repos;

import com.hrms.backend.entities.ExpenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExpenseStatusRepo extends JpaRepository<ExpenseStatus, UUID> {
}
