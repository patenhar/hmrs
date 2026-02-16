package com.hrms.backend.services;

import com.hrms.backend.dtos.request.AddressReqDto;
import com.hrms.backend.dtos.request.TravelReqDto;
import com.hrms.backend.entities.Address;
import com.hrms.backend.entities.City;
import com.hrms.backend.entities.Travel;
import com.hrms.backend.entities.UserTravel;
import com.hrms.backend.repos.*;
import com.hrms.backend.services.interfaces.ITravelService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TravelService implements ITravelService {
    private final TravelRepo travelRepo;
    private final UserTravelRepo userTravelRepo;
    private final AddressRepo addressRepo;
    private final ModelMapper modelMapper;
    private final CityRepo cityRepo;
    private final UserRepo userRepo;

    public TravelService(TravelRepo travelRepo, UserTravelRepo userTravelRepo, AddressRepo addressRepo, ModelMapper modelMapper, CityRepo cityRepo, UserRepo userRepo) {
        this.travelRepo = travelRepo;
        this.userTravelRepo = userTravelRepo;
        this.addressRepo = addressRepo;
        this.modelMapper = modelMapper;
        this.cityRepo = cityRepo;
        this.userRepo = userRepo;
    }

    private Travel findById(UUID id) {
        return travelRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Travel not found"));
    }

    @Override
    public ApiResponse<List<Travel>> getAllTravels() {
        return new ApiResponse<>("All travels fetched successfully", travelRepo.findAll());
    }

    @Override
    public ApiResponse<Travel> getTravelById(UUID id) {
        return new ApiResponse<>("Travel fetched successfully", findById(id));
    }

    @Override
    public ApiResponse<List<Travel>> getTravelByUserId(UUID id) {
        return new ApiResponse<>("Travels for current user fetched successfully", userTravelRepo.findTravelByUserId(id).stream().map(UserTravel::getTravel).toList());
    }

    @Override
    @Transactional
    public ApiResponse<Travel> addTravel(TravelReqDto travelReqDto) {
        Travel travel = modelMapper.map(travelReqDto, Travel.class);
        List<AddressReqDto> addressReqDtos = travelReqDto.getDestinations();
        List<Address> addresses = new ArrayList<>();
        if (addressReqDtos != null){
            for(AddressReqDto addressReqDto: addressReqDtos){
                City city = cityRepo.findById(addressReqDto.getCityId()).orElseThrow(() -> new ResourceNotFoundException("city not found"));
                Address address = modelMapper.map(addressReqDto, Address.class);
                address.setCity(city);
                addresses.add(addressRepo.save(address));
            }
        }
        travel.getDestinations().addAll(addresses);
        Travel t = travelRepo.save(travel);
        for(UUID userId: travelReqDto.getUserIds()){
            UserTravel userTravel = new UserTravel();
            userTravel.setTravel(t);
            userTravel.setUser(userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found")));
        }
        return new ApiResponse<>("Travel planned successfully", t);
    }

    @Override
    public ApiResponse<Travel> updateTravel(UUID id, TravelReqDto roleDto) {
        return null;
    }

    @Override
    public ApiResponse<String> deleteTravel(UUID id) {
        Travel travel = findById(id);
        if (travel.getTravelDate().isBefore(LocalDate.now())) {
            return new ApiResponse<>("This operation is now not allowed", null);
        }
        travelRepo.deleteById(id);
        return new ApiResponse<>("Travel deleted successfully", null);
    }
}
