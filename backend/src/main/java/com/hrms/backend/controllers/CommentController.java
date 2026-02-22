package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.CommentReqDto;
import com.hrms.backend.dtos.request.DeleteWithReasonReqDto;
import com.hrms.backend.dtos.response.CommentResDto;
import com.hrms.backend.services.CommentService;
import com.hrms.backend.services.UserService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/post/{postId}")
//    @PreAuthorize("hasAuthority('VIEW_POST')")
    public ResponseEntity<ApiResponse<List<CommentResDto>>> getCommentsByPost(@PathVariable UUID postId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Comments fetched successfully",
                        commentService.getCommentsByPostId(postId)));
    }

    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_POST')")
    public ResponseEntity<ApiResponse<CommentResDto>> addComment(
            @RequestBody @Validated(OnCreate.class) CommentReqDto dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Comment added successfully", commentService.addComment(dto)));
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('UPDATE_POST')")
    public ResponseEntity<ApiResponse<CommentResDto>> updateComment(
            @PathVariable UUID id,
            @RequestBody @Validated(OnUpdate.class) CommentReqDto dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Comment updated successfully",
                        commentService.updateComment(id, dto)));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('DELETE_POST') or hasAuthority('MANAGE_POST')")
    public ResponseEntity<ApiResponse<String>> deleteComment(
            @PathVariable UUID id,
            @RequestBody(required = false) DeleteWithReasonReqDto reasonDto) {
        String remarks = reasonDto != null ? reasonDto.getRemarks() : null;

        boolean isHrAction = false;
        try {
            isHrAction = userService.getCurrentUser().getRole().getPermissions()
                    .stream()
                    .anyMatch(p -> "MANAGE_POST".equals(p.getPermissionName()));
        } catch (Exception ignored) { }

        String res = commentService.softDeleteComment(id, remarks, isHrAction)
                ? "Comment deleted successfully"
                : "Comment could not be deleted";
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null));
    }
}
