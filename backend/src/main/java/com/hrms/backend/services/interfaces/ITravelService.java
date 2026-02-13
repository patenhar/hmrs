package com.hrms.backend.services.interfaces;

import com.hrms.backend.dtos.request.TravelReqDto;
import com.hrms.backend.entities.Travel;
import com.hrms.backend.utils.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface ITravelService {
    ApiResponse<List<Travel>> getAllTravels();

    ApiResponse<Travel> getTravelById(UUID id);

    ApiResponse<List<Travel>> getTravelByUserId(UUID id);

    ApiResponse<Travel> addTravel(TravelReqDto travelReqDto);

    ApiResponse<Travel> updateTravel(UUID id, TravelReqDto travelReqDto);

    ApiResponse<String> deleteTravel(UUID id);

}
