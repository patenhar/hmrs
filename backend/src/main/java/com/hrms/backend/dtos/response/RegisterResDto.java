package com.hrms.backend.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RegisterResDto {
    private UUID pkUserId;
    private String email;
}