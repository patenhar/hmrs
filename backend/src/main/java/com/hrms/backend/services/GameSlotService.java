package com.hrms.backend.services;

import com.hrms.backend.dtos.request.GameSlotReqDto;
import com.hrms.backend.dtos.response.GameResDto;
import com.hrms.backend.dtos.response.GameSlotResDto;
import com.hrms.backend.entities.Game;
import com.hrms.backend.entities.GameSlot;
import com.hrms.backend.repos.GameRepo;
import com.hrms.backend.repos.GameSlotRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class GameSlotService {
    private final GameSlotRepo gameSlotRepo;
    private final GameRepo gameRepo;
    private final ModelMapper modelMapper;
    private final GameService gameService;

    public GameSlotService(GameSlotRepo gameSlotRepo, GameRepo gameRepo, ModelMapper modelMapper, GameService gameService) {
        this.gameSlotRepo = gameSlotRepo;
        this.gameRepo = gameRepo;
        this.modelMapper = modelMapper;
        this.gameService = gameService;
    }

    public GameSlotResDto findGameSlotById(UUID id) {
        GameSlot gameSlot = gameSlotRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("GameSlot not found"));
        return modelMapper.map(gameSlot, GameSlotResDto.class);
    }

    public List<GameSlotResDto> getAllGameSlots() {
        return gameSlotRepo.findAll().stream().map(st -> modelMapper.map(st, GameSlotResDto.class)).toList();
    }

    public GameSlotResDto getGameSlotById(UUID id) {
        return findGameSlotById(id);
    }

    public List<GameSlotResDto> getGameSlotsByGameAndDate(UUID gameId, LocalDate date) {
        return gameSlotRepo.findAvailableSlots(gameId, date).stream().map(gs -> modelMapper.map(gs, GameSlotResDto.class)).toList();
    }

    @Async
//    @Scheduled(cron = "0 1 0 * * MON-FRI")
//    @Scheduled(cron = "0 * * * * *")
    public void addGameSlot() {
        LocalDate date = LocalDate.now();
        List<Game> games = gameRepo.findAll();
        for(Game game: games) {
            long operatingHours = Duration.between(game.getOperationHourBegin(), game.getOperationHourEnd()).toHours();
            int numberOfSlots = (int)(operatingHours / game.getDuration());
            long seconds = (long) (game.getDuration() * 3600);

            LocalTime slotTime = game.getOperationHourBegin();
            while (numberOfSlots != 0) {
                GameSlot gameSlot = new GameSlot();
                gameSlot.setDate(date);
                gameSlot.setGame(game);
                gameSlot.setBeginTime(slotTime);
                slotTime = slotTime.plus(Duration.ofSeconds(seconds));
                gameSlot.setEndTime(slotTime);
                gameSlotRepo.save(gameSlot);
                log.info("Slot added");
                numberOfSlots--;
            }
        }
    }
}
