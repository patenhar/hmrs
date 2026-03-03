package com.hrms.backend.entities;

import com.hrms.backend.enums.ExpenseStatus;
import com.hrms.backend.enums.ExpenseType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_document_id", referencedColumnName = "pkDocumentId")
    private Document document;

    @ManyToOne
    @JoinColumn(name = "fk_user_travel_id", referencedColumnName = "pkUserTravelId")
    private UserTravel userTravel;

    @Enumerated(EnumType.STRING)
    private ExpenseType expenseType;

    private LocalDateTime lastActionAt;

    @ManyToOne
    @JoinColumn(name = "fk_last_action_by", referencedColumnName = "pkUserId")
    private User lastActionBy;
    
    private String remarks;

    @Enumerated(EnumType.STRING)
    private ExpenseStatus expenseStatus;
}
