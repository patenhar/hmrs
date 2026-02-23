package com.hrms.backend.dtos.request;

import java.util.UUID;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthReqDto {
    @NotBlank(message = "Email is required", groups = {OnCreate.class, OnUpdate.class})
    @Email(message = "Invalid email format", groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @NotBlank(message = "Password is required", groups = {OnCreate.class, OnUpdate.class})
    private String password;

    private UUID roleId;
}
