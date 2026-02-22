package com.hrms.backend.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserTravelResDtoForTravel {
    private UUID pkUserTravelId;
    private UserResDto user;
}
