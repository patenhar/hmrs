package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class JobStakeHolderReqDto {
    @NotBlank(message = "User is required", groups = {OnCreate.class, OnUpdate.class})
    private UUID userId;

    @NotBlank(message = "Stakeholder type is required", groups = {OnCreate.class, OnUpdate.class})
    private UUID jobStakeHolderTypeId;
}
