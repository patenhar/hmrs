package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTravelReqDto {
    @NotNull(message = "User is required", groups = {OnCreate.class, OnUpdate.class})
    private UUID userId;

    @NotNull(message = "Travel is required", groups = {OnCreate.class, OnUpdate.class})
    private UUID travelId;
}
