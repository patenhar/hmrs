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
public class GameBookingInDto {
    private UUID userId;
    private String gameName;
    private String statusName;
    private String dateFrom;
    private String dateTo;
    private Integer page;
    private Integer size;
    private String sort;
}
