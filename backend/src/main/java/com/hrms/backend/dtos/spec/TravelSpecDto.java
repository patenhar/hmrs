package com.hrms.backend.dtos.spec;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelSpecDto {
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
}
