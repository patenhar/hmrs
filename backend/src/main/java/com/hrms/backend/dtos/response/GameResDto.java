package com.hrms.backend.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class GameResDto {
    private UUID pkGameId;
    private String gameName;
    private int maxPlayers;
    private Double duration;
    private LocalTime operationHourBegin;
    private LocalTime operationHourEnd;
    private int version;
    private int bookingCycle;
}
