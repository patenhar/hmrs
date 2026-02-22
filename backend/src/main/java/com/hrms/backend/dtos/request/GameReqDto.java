package com.hrms.backend.dtos.request;

import com.hrms.backend.entities.Profile;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GameReqDto {
    @NotBlank(message = "Game name is required", groups = {OnCreate.class, OnUpdate.class})
    private String gameName;

    @NotNull(message = "Max players is require", groups = {OnCreate.class, OnUpdate.class})
    private int maxPlayers;

    @NotNull(message = "Duration is require", groups = {OnCreate.class, OnUpdate.class})
    private Double duration;

    @NotNull(message = "Operation hour begin time is require", groups = {OnCreate.class, OnUpdate.class})
    private LocalTime operationHourBegin;

    @NotNull(message = "Operation hour end time is require", groups = {OnCreate.class, OnUpdate.class})
    private LocalTime operationHourEnd;

    @NotNull(message = "Booking cycle is require", groups = {OnCreate.class, OnUpdate.class})
    private int bookingCycle;
}
