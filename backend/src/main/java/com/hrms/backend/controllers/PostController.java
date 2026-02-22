package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.DeleteWithReasonReqDto;
import com.hrms.backend.dtos.request.PostReqDto;
import com.hrms.backend.dtos.response.PostResDto;
import com.hrms.backend.services.PostService;
import com.hrms.backend.services.UserService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_POST')")
    public ResponseEntity<ApiResponse<List<PostResDto>>> getAllPosts(
            @RequestParam(required = false) String authorId,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Posts fetched successfully",
                        postService.getAllPosts(authorId, tag, from, to)));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_POST')")
    public ResponseEntity<ApiResponse<PostResDto>> getPostById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Post fetched successfully", postService.getPostById(id)));
    }

    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_POST')")
    public ResponseEntity<ApiResponse<PostResDto>> createPost(
            @RequestBody @Validated(OnCreate.class) PostReqDto dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Post created successfully", postService.createPost(dto)));
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('UPDATE_POST')")
    public ResponseEntity<ApiResponse<PostResDto>> updatePost(
            @PathVariable UUID id,
            @RequestBody @Validated(OnUpdate.class) PostReqDto dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Post updated successfully", postService.updatePost(id, dto)));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('DELETE_POST') or hasAuthority('MANAGE_POST')")
    public ResponseEntity<ApiResponse<String>> deletePost(
            @PathVariable UUID id,
            @RequestBody(required = false) DeleteWithReasonReqDto reasonDto) {
        String remarks = reasonDto != null ? reasonDto.getRemarks() : null;

        // check if current user is HR (has MANAGE_POST) — determine from authorities
        boolean isHrAction = false;
        try {
            isHrAction = userService.getCurrentUser().getRole().getPermissions()
                    .stream()
                    .anyMatch(p -> "MANAGE_POST".equals(p.getPermissionName()));
        } catch (Exception ignored) { }

        String res = postService.softDeletePost(id, remarks, isHrAction)
                ? "Post deleted successfully"
                : "Post could not be deleted";
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null));
    }

    @PostMapping("/{id}/like")
//    @PreAuthorize("hasAuthority('VIEW_POST')")
    public ResponseEntity<ApiResponse<Boolean>> toggleLike(@PathVariable UUID id) {
        boolean liked = postService.toggleLike(id);
        String message = liked ? "Post liked" : "Post unliked";
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(message, liked));
    }
}
