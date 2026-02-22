package com.hrms.backend.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ExpenseStatusResDto {
    private UUID pkExpenseStatusId;
    private String expenseStatusName;
}
