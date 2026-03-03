package com.hrms.backend.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseInDto {
    private UUID userTravelId;
    private String description;
    private String type;
    private String status;
    private String actor;
    private Double amountMin;
    private Double amountMax;
    private Integer page;
    private Integer size;
    private String sort;
}
