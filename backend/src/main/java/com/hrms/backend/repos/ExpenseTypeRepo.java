package com.hrms.backend.repos;

import com.hrms.backend.entities.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExpenseTypeRepo extends JpaRepository<ExpenseType, UUID> {
    List<ExpenseType> findExpenseTypesByExpenseTypeNameContainingIgnoreCase(String expenseTypeName);
}
