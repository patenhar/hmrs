package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PostReqDto {
    @NotBlank(message = "Title is required", groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @NotBlank(message = "Description is required", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    private List<String> tags = new ArrayList<>();

    @NotNull(message = "Visibility is required", groups = {OnCreate.class})
    private UUID visibilityId;
}
