package com.hrms.backend.dtos.spec;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseSpecDto {
    private UUID userTravelId;
    private String description;
    private String type;
    private String status;
    private String actor;
    private Double amountMin;
    private Double amountMax;
}
