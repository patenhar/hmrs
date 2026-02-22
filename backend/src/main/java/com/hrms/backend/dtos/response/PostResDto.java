package com.hrms.backend.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PostResDto {
    private UUID pkPostId;
    private String title;
    private String description;
    private Boolean isDeleted;
    private Boolean isSystemGenerated;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResDto author;
    private PostVisibilityResDto visibility;
    private List<TagResDto> tags;
    private long likeCount;
    private long commentCount;
    private boolean likedByCurrentUser;
    private List<UserResDto> recentLikers;
    private List<CommentResDto> comments;
}
