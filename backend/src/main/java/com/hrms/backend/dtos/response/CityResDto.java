package com.hrms.backend.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CityResDto {
    private UUID pkCityId;
    private String cityName;
    private CountryResDto country;
}
