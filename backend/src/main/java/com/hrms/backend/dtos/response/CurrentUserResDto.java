package com.hrms.backend.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CurrentUserResDto {
    private UUID pkUserId;
    private String email;
    private RoleResDto role;
    private List<String> authorities;
}
