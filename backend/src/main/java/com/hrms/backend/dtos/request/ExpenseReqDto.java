package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ExpenseReqDto {
    @NotNull(message = "Expense amount is required", groups = {OnCreate.class, OnUpdate.class})
    @Min(value = 0, message = "Amount cannot be less than zero", groups = {OnCreate.class, OnUpdate.class})
    private Double amount;

    @NotBlank(message = "Description is required", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @Valid
    private DocumentReqDto documentReqDto;

    @NotNull(message = "Travel details are required", groups = {OnCreate.class, OnUpdate.class})
    private UUID userTravelId;

    @NotNull(message = "Expense type is required", groups = {OnCreate.class, OnUpdate.class})
    private UUID expenseTypeId;
}
