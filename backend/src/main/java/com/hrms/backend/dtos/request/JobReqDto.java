package com.hrms.backend.dtos.request;

import com.hrms.backend.entities.JobStakeHolder;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobReqDto {
    @NotBlank(message = "Title is required", groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @NotBlank(message = "Description is required", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @NotNull(message = "JD is required", groups = {OnCreate.class, OnUpdate.class})
    @Valid
    private DocumentReqDto documentReqDto;

    @NotNull(message = "At least one stakeholder is required", groups = {OnCreate.class, OnUpdate.class})
    private List<@Valid JobStakeHolderReqDto> jobStakeHolderReqDtos;
}
