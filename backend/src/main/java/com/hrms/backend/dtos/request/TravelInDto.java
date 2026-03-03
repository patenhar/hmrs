package com.hrms.backend.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelInDto {
    private String title;
    private String description;
    private LocalDate travelDate;
    private LocalDate returnDate;
    private String travelDateKeyword;
    private String returnDateKeyword;
    private Double maxGrantPerDay;
    private Double maxGrantPerDayMin;
    private Double maxGrantPerDayMax;
    private String hrMail;
    private Integer page;
    private Integer size;
    private String sort;

}
