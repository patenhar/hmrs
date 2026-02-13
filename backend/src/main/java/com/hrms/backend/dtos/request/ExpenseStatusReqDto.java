package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.NotBlank;

public class ExpenseStatusReqDto {
    @NotBlank(message = "Expense status name is required", groups = {OnCreate.class, OnUpdate.class})
    private String expenseStatusName;
}
