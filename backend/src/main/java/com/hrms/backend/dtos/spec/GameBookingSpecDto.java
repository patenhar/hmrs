package com.hrms.backend.dtos.spec;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameBookingSpecDto {
    private UUID userId;
    private String gameName;
    private String statusName;
    private LocalDate dateFrom;
    private LocalDate dateTo;
}
