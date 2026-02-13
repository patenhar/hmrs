package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Expense_types")
@Getter @Setter
public class ExpenseType extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkExpenseTypeId;

    @NotBlank
    private String expenseTypeName;

    @OneToMany(mappedBy = "expenseType")
    private List<Expense> expenses = new ArrayList<>();
}
