package com.hrms.backend.dtos.response;

import com.hrms.backend.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProfileResDto {
    private UUID pkProfileId;
    private String name;
    private LocalDate birthDate;
    private LocalDate joiningDate;
    private UserResDtoForProfile user;
    private ProfileResDtoForManager managerProfile;
    private DepartmentResDto department;
    private ProfileStatus profileStatus;
    private List<GameResDto> games;
}
