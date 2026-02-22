package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class GameSlotReqDto {
    @NotNull(message = "Date is required", groups = {OnCreate.class, OnUpdate.class})
    private LocalDate date;

    @NotNull(message = "Game is required", groups = {OnCreate.class, OnUpdate.class})
    private UUID gameId;
}
