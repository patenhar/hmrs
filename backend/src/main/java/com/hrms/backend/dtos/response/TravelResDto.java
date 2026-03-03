package com.hrms.backend.dtos.response;

import com.hrms.backend.entities.UserTravel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TravelResDto {
    private UUID pkTravelId;
    private String title;
    private String description;
    private LocalDate travelDate;
    private LocalDate returnDate;
    private double maxGrantPerDay;
    private String hrMail;
    private List<AddressResDto> destinations;
    private List<UserTravelResDtoForTravel> userTravels;
    private Boolean isDeleted;
}
