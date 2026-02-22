package com.hrms.backend.services;

import com.hrms.backend.dtos.request.GameReqDto;
import com.hrms.backend.dtos.response.GameResDto;
import com.hrms.backend.entities.Game;
import com.hrms.backend.repos.GameRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GameService {
    private final GameRepo gameRepo;
    private final ModelMapper modelMapper;

    public GameService(GameRepo gameRepo, ModelMapper modelMapper) {
        this.gameRepo = gameRepo;
        this.modelMapper = modelMapper;
    }

    public GameResDto findGameById(UUID id) {
        Game game = gameRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Game not found"));
        return modelMapper.map(game, GameResDto.class);
    }

    public List<GameResDto> getAllGames() {
        return gameRepo.findAll().stream().map(st -> modelMapper.map(st, GameResDto.class)).toList();
    }

    public List<GameResDto> getGamesByGameNameContainingIgnoreCase(String name) {
        return gameRepo.findGamesByGameNameContainingIgnoreCase(name).stream().map(st -> modelMapper.map(st, GameResDto.class)).toList();
    }

    public GameResDto getGameById(UUID id) {
        return findGameById(id);
    }

    public GameResDto addGame(GameReqDto gameReqDto) {
        Game game =  gameRepo.save(modelMapper.map(gameReqDto, Game.class));
        return modelMapper.map(game, GameResDto.class);
    }

    public GameResDto updateGame(UUID id, GameReqDto gameReqDto) {
        GameResDto game = findGameById(id);
        modelMapper.map(gameReqDto, game);
        Game updatedGame =  gameRepo.save(modelMapper.map(gameReqDto, Game.class));
        return modelMapper.map(updatedGame, GameResDto.class);
    }

    public boolean deleteGame(UUID id) {
        findGameById(id);
        gameRepo.deleteById(id);
        return true;
    }
}
