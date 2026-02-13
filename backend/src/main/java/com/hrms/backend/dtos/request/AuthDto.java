package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuthDto {
    @NotBlank(message = "Email is required", groups = {OnCreate.class, OnUpdate.class})
    @Email(message = "Invalid email format", groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @NotBlank(message = "Password is required", groups = {OnCreate.class, OnUpdate.class})
//    @Password
    private String password;
}
