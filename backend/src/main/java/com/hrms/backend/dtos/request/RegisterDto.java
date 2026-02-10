package com.hrms.backend.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegisterDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
