package com.hrms.backend.services;

import com.hrms.backend.dtos.request.GameInDto;
import com.hrms.backend.dtos.request.GameReqDto;
import com.hrms.backend.dtos.response.GameResDto;
import com.hrms.backend.dtos.spec.GameSpecDto;
import com.hrms.backend.entities.Game;
import com.hrms.backend.events.GameConfigUpdateEvent;
import com.hrms.backend.repos.GameRepo;
import com.hrms.backend.utils.GameSpecification;
import com.hrms.backend.utils.JsonStringToSortDto;
import com.hrms.backend.utils.ResourceNotFoundException;
import com.hrms.backend.dtos.spec.SortDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class GameService {
    private final GameRepo gameRepo;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public GameService(GameRepo gameRepo, ModelMapper modelMapper, ApplicationEventPublisher applicationEventPublisher) {
        this.gameRepo = gameRepo;
        this.modelMapper = modelMapper;
        this.applicationEventPublisher = applicationEventPublisher;
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
        Game game = gameRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found"));
        int oldVersion = game.getVersion();
        if(!Objects.equals(game.getDuration(), gameReqDto.getDuration()) || game.getOperationHourBegin() != gameReqDto.getOperationHourBegin() || game.getOperationHourEnd() != gameReqDto.getOperationHourEnd()) {
            game.setVersion(oldVersion + 1);
        }
        modelMapper.map(gameReqDto, game);
        Game updatedGame = gameRepo.save(game);
        log.info(String.valueOf(oldVersion));
        log.info(String.valueOf(updatedGame.getVersion()));
        if(oldVersion != updatedGame.getVersion()) {
            applicationEventPublisher.publishEvent(new GameConfigUpdateEvent(this, updatedGame.getPkGameId()));
        }
        return modelMapper.map(updatedGame, GameResDto.class);
    }

    public boolean deleteGame(UUID id) {
        findGameById(id);
        gameRepo.deleteById(id);
        return true;
    }

    public Page<GameResDto> searchGamesWithPaginationSortingAndFiltering(GameInDto dto) {
        GameSpecDto specDto = GameSpecDto.builder()
                .gameName(dto.getGameName())
                .maxPlayersMin(dto.getMaxPlayersMin())
                .maxPlayersMax(dto.getMaxPlayersMax())
                .build();

        List<SortDto> sortDtos = JsonStringToSortDto.jsonStringToSortDto(dto.getSort());
        List<Sort.Order> orders = new ArrayList<>();
        if (sortDtos != null) {
            for (SortDto sortDto : sortDtos) {
                Sort.Direction direction = Objects.equals(sortDto.getDirection(), "desc")
                        ? Sort.Direction.DESC : Sort.Direction.ASC;
                orders.add(new Sort.Order(direction, sortDto.getField()));
            }
        }

        PageRequest pageRequest = PageRequest.of(
                dto.getPage(),
                dto.getSize(),
                Sort.by(orders)
        );

        Specification<Game> specification = GameSpecification.getSpecification(specDto);
        Page<Game> games = gameRepo.findAll(specification, pageRequest);
        return games.map(g -> modelMapper.map(g, GameResDto.class));
    }
}
