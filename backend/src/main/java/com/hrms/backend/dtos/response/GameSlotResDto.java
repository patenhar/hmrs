package com.hrms.backend.dtos.response;

import com.hrms.backend.entities.Game;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class GameSlotResDto {
    private UUID pkGameSlotId;
    private LocalDate date;
    private LocalTime beginTime;
    private LocalTime endTime;
    private Game game;
}
