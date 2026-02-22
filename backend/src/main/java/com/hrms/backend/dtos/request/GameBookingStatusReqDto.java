package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GameBookingStatusReqDto {
    @NotBlank(message = "Game booking status name required", groups = {OnCreate.class, OnUpdate.class})
    private String gameBookingStatusName;
}
