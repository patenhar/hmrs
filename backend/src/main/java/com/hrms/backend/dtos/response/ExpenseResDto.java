package com.hrms.backend.dtos.response;

import com.hrms.backend.entities.Document;
import com.hrms.backend.entities.ExpenseStatus;
import com.hrms.backend.entities.ExpenseType;
import com.hrms.backend.entities.UserTravel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ExpenseResDto {
    private UUID pkExpenseId;
    private double amount;
    private String description;
    private DocumentResDto document;
    private UserTravelResDto userTravel;
    private ExpenseTypeResDto expenseType;
    private ExpenseStatusResDto expenseStatus;
}
