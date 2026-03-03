package com.hrms.backend.dtos.response;

import com.hrms.backend.entities.Department;
import com.hrms.backend.entities.Profile;
import com.hrms.backend.entities.User;
import com.hrms.backend.enums.ProfileStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProfileResDtoForUser {
    private UUID pkProfileId;
    private String name;
    private LocalDate birthDate;
    private LocalDate joiningDate;
    private DepartmentResDto department;
    private ProfileStatus profileStatus;
}
