package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryReqDto {
    @NotBlank(message = "Country name is required", groups = {OnCreate.class, OnUpdate.class})
    private String countryName;
}
