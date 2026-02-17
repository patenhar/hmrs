package com.hrms.backend.dtos.response;

import com.hrms.backend.entities.Role;

import java.util.UUID;

public class UserResDto {
    private UUID pkUserId;
    private String email;
    private RoleResDto role;
}
