package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Expense_statuses")
@Getter @Setter
public class ExpenseStatus extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkExpenseStatusId;

    @NotBlank
    private String expenseStatusName;

    @OneToMany(mappedBy = "expenseStatus")
    private List<Expense> expenses = new ArrayList<>();
}
