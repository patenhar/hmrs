package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UUID;

@Getter @Setter
public class AddressReqDto {
    @NotBlank(message = "Address line 1 is required", groups = {OnCreate.class, OnUpdate.class})
    private String addressLine1;

    private String addressLine2;

    @NotBlank(message = "City is required", groups = {OnCreate.class, OnUpdate.class})
    @UUID(message = "Invalid UUID format")
    private java.util.UUID cityId;
}
