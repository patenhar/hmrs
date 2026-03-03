package com.hrms.backend.listeners;

import com.hrms.backend.dtos.response.GameBookingResDto;
import com.hrms.backend.dtos.response.GameResDto;
import com.hrms.backend.dtos.response.GameSlotResDto;
import com.hrms.backend.entities.Game;
import com.hrms.backend.entities.GameSlot;
import com.hrms.backend.events.GameConfigUpdateEvent;
import com.hrms.backend.repos.GameSlotRepo;
import com.hrms.backend.services.GameBookingService;
import com.hrms.backend.services.GameService;
import com.hrms.backend.services.GameSlotService;
import com.hrms.backend.services.UserService;
import com.hrms.backend.utils.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
public class GameConfigUpdateEventListener implements ApplicationListener<GameConfigUpdateEvent> {
    private final GameService gameService;
    private final GameBookingService gameBookingService;
    private final GameSlotService gameSlotService;
    private final ModelMapper modelMapper;
    private final GameSlotRepo gameSlotRepo;
    private final UserService userService;

    public GameConfigUpdateEventListener(GameService gameService, GameBookingService gameBookingService, GameSlotService gameSlotService, ModelMapper modelMapper, GameSlotRepo gameSlotRepo, UserService userService) {
        this.gameService = gameService;
        this.gameBookingService = gameBookingService;
        this.gameSlotService = gameSlotService;
        this.modelMapper = modelMapper;
        this.gameSlotRepo = gameSlotRepo;
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(GameConfigUpdateEvent gameConfigUpdateEvent) {
        GameResDto game = gameService.findGameById(gameConfigUpdateEvent.getPkGameId());
        LocalDate eventDate = LocalDate.now();
        LocalTime eventTime = LocalTime.now();
        List<GameSlotResDto> gameSlots = gameSlotService.getSlotsByGameDateAndTime(game.getPkGameId(), eventDate, eventTime);
        while(!gameSlots.isEmpty()) {
            long operatingHours = Duration.between(game.getOperationHourBegin(), game.getOperationHourEnd()).toHours();
            int numberOfSlots = (int)(operatingHours / game.getDuration());
            long seconds = (long) (game.getDuration() * 3600);

            if(numberOfSlots == gameSlots.size()) {
                LocalTime slotTime = game.getOperationHourBegin();
                for (GameSlotResDto gameSlot: gameSlots ){
                    GameSlot existingSlot = gameSlotRepo.findById(gameSlot.getPkGameSlotId())
                            .orElseThrow(() -> new ResourceNotFoundException("GameSlot not found"));
                    existingSlot.setBeginTime(slotTime);
                    slotTime = slotTime.plus(Duration.ofSeconds(seconds));
                    existingSlot.setEndTime(slotTime);
                    gameSlotRepo.save(existingSlot);
                }
                return;
            }
            else {
                List<GameBookingResDto> bookings = gameBookingService.getBookingsByGameDateAndTime(game.getPkGameId(), eventDate, eventTime);
                for(GameBookingResDto booking: bookings) {
                    gameBookingService.cancelBooking(booking.getPkGameBookingId());
                }
                gameSlotRepo.deleteAllById(gameSlots.stream().map(GameSlotResDto::getPkGameSlotId).toList());

                Game gameEntity = modelMapper.map(game, Game.class);
                LocalTime slotStart = game.getOperationHourBegin();
                while (slotStart.isBefore(game.getOperationHourEnd())) {
                    LocalTime slotEnd = slotStart.plus(Duration.ofSeconds(seconds));
                    if (slotStart.isAfter(eventTime)) {
                        GameSlot newSlot = new GameSlot();
                        newSlot.setDate(eventDate);
                        newSlot.setGame(gameEntity);
                        newSlot.setBeginTime(slotStart);
                        newSlot.setEndTime(slotEnd);
                        gameSlotRepo.save(newSlot);
                    }
                    slotStart = slotEnd;
                }
                gameSlots = gameSlotService.getSlotsByGameDateAndTime(game.getPkGameId(), eventDate, eventTime);
                if (!gameSlots.isEmpty()) {
                    int bookingNumber = 0;
                    for (GameBookingResDto booking : bookings) {
                        gameBookingService.addAutoBooking(modelMapper.map(gameSlots.get(bookingNumber % gameSlots.size()), GameSlot.class), booking.getTeamMembers().stream().map(u -> userService.findById(u.getPkUserId())).toList());
                        bookingNumber++;
                    }
                }
            }
            eventDate = eventDate.plusDays(1);
            eventTime = LocalTime.parse("12:01");
            gameSlots = gameSlotService.getSlotsByGameDateAndTime(game.getPkGameId(), eventDate, eventTime);
        }

        log.info(String.valueOf(gameConfigUpdateEvent.getPkGameId()));
    }
}
