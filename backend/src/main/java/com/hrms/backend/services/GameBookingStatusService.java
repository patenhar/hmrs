package com.hrms.backend.services;

import com.hrms.backend.dtos.request.GameBookingStatusReqDto;
import com.hrms.backend.dtos.response.GameBookingStatusResDto;
import com.hrms.backend.entities.GameBookingStatus;
import com.hrms.backend.repos.GameBookingStatusRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GameBookingStatusService {

    private final GameBookingStatusRepo gameBookingStatusRepo;
    private final ModelMapper modelMapper;

    public GameBookingStatusService(GameBookingStatusRepo gameBookingStatusRepo, ModelMapper modelMapper) {
        this.gameBookingStatusRepo = gameBookingStatusRepo;
        this.modelMapper = modelMapper;
    }

    public GameBookingStatusResDto findGameBookingStatusById(UUID id) {
        GameBookingStatus gameBookingStatus = gameBookingStatusRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("GameBooking status not found"));
        return modelMapper.map(gameBookingStatus, GameBookingStatusResDto.class);
    }

    public List<GameBookingStatusResDto> getAllGameBookingStatuses() {
        return gameBookingStatusRepo.findAll().stream().map(st -> modelMapper.map(st, GameBookingStatusResDto.class)).toList();
    }

    public GameBookingStatusResDto getGameBookingStatusById(UUID id) {
        return findGameBookingStatusById(id);
    }

    public GameBookingStatusResDto addGameBookingStatus(GameBookingStatusReqDto gameBookingStatusReqDto) {
        GameBookingStatus gameBookingStatus = gameBookingStatusRepo.save(modelMapper.map(gameBookingStatusReqDto, GameBookingStatus.class));
        return modelMapper.map(gameBookingStatus, GameBookingStatusResDto.class);
    }

    public GameBookingStatusResDto updateGameBookingStatus(UUID id, GameBookingStatusReqDto gameBookingStatusReqDto) {
        GameBookingStatusResDto gameBookingStatus = findGameBookingStatusById(id);
        modelMapper.map(gameBookingStatusReqDto, gameBookingStatus);
        GameBookingStatus updatedGameBookingStatus = gameBookingStatusRepo.save(modelMapper.map(gameBookingStatusReqDto, GameBookingStatus.class));
        return modelMapper.map(updatedGameBookingStatus, GameBookingStatusResDto.class);
    }

    public boolean deleteGameBookingStatus(UUID id) {
        findGameBookingStatusById(id);
        gameBookingStatusRepo.deleteById(id);
        return true;
    }
}
