package com.hrms.backend.dtos.request;

import com.hrms.backend.entities.Department;
import com.hrms.backend.entities.Profile;
import com.hrms.backend.entities.ProfileStatus;
import com.hrms.backend.entities.User;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProfileReqDto {
    @NotBlank(message = "Name is required", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(message = "Birth date is required", groups = {OnCreate.class, OnUpdate.class})
    @Past(message = "Birth date must be in the past", groups = {OnCreate.class, OnUpdate.class})
    private LocalDate birthDate;

    @NotNull(message = "Joining date is required", groups = {OnCreate.class, OnUpdate.class})
    @Past(message = "Joining date must be in the past", groups = {OnCreate.class, OnUpdate.class})
    private LocalDate joiningDate;

    private UUID managerProfileId;

    @NotNull(message = "Department is required", groups = {OnCreate.class, OnUpdate.class})
    private UUID departmentId;

    @NotNull(message = "Profile status is required", groups = {OnUpdate.class})
    private UUID profileStatusId;
}
