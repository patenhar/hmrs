package com.hrms.backend.dtos.response;

import com.hrms.backend.enums.ExpenseStatus;
import com.hrms.backend.enums.ExpenseType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ExpenseResDto {
    private UUID pkExpenseId;
    private double amount;
    private String description;
    private DocumentResDto document;
    private UserTravelResDto userTravel;
    private ExpenseType expenseType;
    private ExpenseStatus expenseStatus;
    private LocalDateTime lastActionAt;
    private UserResDto lastActionBy;
    private String remarks;
}
