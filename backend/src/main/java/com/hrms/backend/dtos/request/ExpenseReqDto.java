package com.hrms.backend.dtos.request;

import com.hrms.backend.entities.UserTravel;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.UUID;

@Getter
@Setter
public class ExpenseReqDto {
    @NotNull(message = "Expense amount is required", groups = {OnCreate.class, OnUpdate.class})
    @Min(value = 0, message = "Amount cannot be less than zero", groups = {OnCreate.class, OnUpdate.class})
    private double amount;

    @NotBlank(message = "Description is required", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    private DocumentReqDto documentReqDto;

    @NotBlank(message = "Travel details are required", groups = {OnCreate.class, OnUpdate.class})
    @UUID(message = "Invalid UUID format")
    private java.util.UUID userTravelId;

    @NotBlank(message = "Expense type is required", groups = {OnCreate.class, OnUpdate.class})
    @UUID(message = "Invalid UUID format")
    private java.util.UUID expenseTypeId;
}
