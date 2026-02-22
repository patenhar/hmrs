package com.hrms.backend.dtos.response;

import com.hrms.backend.entities.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserResDto {
    private UUID pkUserId;
    private String email;
    private RoleResDto role;
}
