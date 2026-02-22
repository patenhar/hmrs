package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GameBookingReqDto {
    @NotNull(message = "Game slot is required", groups = {OnCreate.class, OnUpdate.class})
    private UUID gameSlotId;

    @NotNull(message = "At least one team member is required", groups = {OnCreate.class, OnUpdate.class})
    private List<UUID> teamMemberIds;
}
