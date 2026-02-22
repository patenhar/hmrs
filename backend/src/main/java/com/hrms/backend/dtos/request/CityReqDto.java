package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CityReqDto {
    @NotBlank(message = "City name is required", groups = {OnCreate.class, OnUpdate.class})
    private String cityName;

    @NotNull(message = "Country is required", groups = {OnCreate.class, OnUpdate.class})
    private UUID countryId;
}
