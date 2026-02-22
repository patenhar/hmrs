package com.hrms.backend.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddressResDto {
    private UUID pkAddressId;
    private String addressLine1;
    private String addressLine2;
    private CityResDto city;
}
