package com.hrms.backend.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CommentResDto {
    private UUID pkCommentId;
    private String content;
    private UserResDto author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;
    private List<CommentResDto> replies;
}
