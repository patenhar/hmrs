package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReferralReqDto {
    @NotBlank(message = "Name is required", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotBlank(message = "Email is required", groups = {OnCreate.class, OnUpdate.class})
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Note is required", groups = {OnCreate.class, OnUpdate.class})
    private String note;

    @Valid
    private DocumentReqDto documentReqDto;
}
