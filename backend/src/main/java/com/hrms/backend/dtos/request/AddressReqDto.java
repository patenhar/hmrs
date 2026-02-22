package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter @Setter
public class AddressReqDto {
    @NotBlank(message = "Address line 1 is required", groups = {OnCreate.class, OnUpdate.class})
    private String addressLine1;

    private String addressLine2;

    @NotNull(message = "City is required", groups = {OnCreate.class, OnUpdate.class})
    private UUID cityId;
}
