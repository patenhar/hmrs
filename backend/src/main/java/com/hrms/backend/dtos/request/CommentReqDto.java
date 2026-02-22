package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CommentReqDto {
    @NotNull(message = "Post ID is required", groups = {OnCreate.class})
    private UUID postId;

    private UUID parentCommentId;

    @NotBlank(message = "Content is required", groups = {OnCreate.class, OnUpdate.class})
    private String content;
}
