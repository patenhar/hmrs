package com.hrms.backend.dtos.response;

import com.hrms.backend.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProfileResDtoForManager {
    private UUID pkProfileId;
    private String name;
    private DepartmentResDto department;
    private ProfileStatus profileStatus;
}
