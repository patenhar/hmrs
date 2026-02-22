package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "Expenses")
@Getter @Setter
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkExpenseId;

    @Positive
    private Double amount;

    @NotBlank
    private String description;

    @OneToOne
    @JoinColumn(name = "fk_document_id", referencedColumnName = "pkDocumentId")
    private Document document;

    @ManyToOne
    @JoinColumn(name = "fk_user_travel_id", referencedColumnName = "pkUserTravelId")
    private UserTravel userTravel;

    @ManyToOne
    @JoinColumn(name = "fk_expense_type_id", referencedColumnName = "pkExpenseTypeId")
    private ExpenseType expenseType;

    @ManyToOne
    @JoinColumn(name = "fk_expense_status_id", referencedColumnName = "pkExpenseStatusId")
    private ExpenseStatus expenseStatus;
}
