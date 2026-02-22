package com.hrms.backend.controllers;

import com.hrms.backend.dtos.response.PostVisibilityResDto;
import com.hrms.backend.services.PostVisibilityService;
import com.hrms.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/post-visibilities")
public class PostVisibilityController {
    private final PostVisibilityService postVisibilityService;

    @Autowired
    public PostVisibilityController(PostVisibilityService postVisibilityService) {
        this.postVisibilityService = postVisibilityService;
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<PostVisibilityResDto>>> getAllVisibilities() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Post visibilities fetched successfully",
                        postVisibilityService.getAllVisibilities()));
    }
}
