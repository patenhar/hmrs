package com.hrms.backend.services;

import com.hrms.backend.dtos.request.UserTravelReqDto;
import com.hrms.backend.dtos.response.UserTravelResDto;
import com.hrms.backend.dtos.response.UserTravelResDtoForTravel;
import com.hrms.backend.entities.Travel;
import com.hrms.backend.entities.User;
import com.hrms.backend.entities.UserTravel;
import com.hrms.backend.repos.TravelRepo;
import com.hrms.backend.repos.UserTravelRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserTravelService {
    private final UserTravelRepo userTravelRepo;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final TravelRepo travelRepo;

    public UserTravelService(UserTravelRepo userTravelRepo, ModelMapper modelMapper, UserService userService, TravelRepo travelRepo) {
        this.userTravelRepo = userTravelRepo;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.travelRepo = travelRepo;
    }

    public UserTravelResDto findUserTravelById(UUID id) {
        UserTravel userTravel = userTravelRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense type is not found"));
        return modelMapper.map(userTravel, UserTravelResDto.class);
    }

    public List<UserTravelResDto> getAllUserTravels() {
        return userTravelRepo.findAll().stream().map(st -> modelMapper.map(st, UserTravelResDto.class)).toList();
    }

    public UserTravelResDto getUserTravelById(UUID id) {
        return findUserTravelById(id);
    }

    public List<UserTravelResDtoForTravel> getTravelUsers(UUID id) {
        return userTravelRepo.findUserTravelsByTravelPkTravelId(id);
    }

    public UserTravelResDto addUserTravel(UserTravelReqDto userTravelReqDto) {
        UserTravel userTravel = new UserTravel();
        userTravel.setUser(modelMapper.map(userService.findUserById(userTravelReqDto.getUserId()), User.class));
        userTravel.setTravel(travelRepo.findById(userTravelReqDto.getTravelId()).orElseThrow(() -> new ResourceNotFoundException("Travel not found")));
        return modelMapper.map(userTravelRepo.save(userTravel), UserTravelResDto.class);
    }

    public UserTravelResDto saveUserTravel(UUID userId, Travel travel){
        UserTravel userTravel = new UserTravel();
        userTravel.setUser(modelMapper.map(userService.findUserById(userId), User.class));
        userTravel.setTravel(travel);
        return modelMapper.map(userTravelRepo.save(userTravel), UserTravelResDto.class);
    }
}
