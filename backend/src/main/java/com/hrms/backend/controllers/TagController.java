package com.hrms.backend.controllers;

import com.hrms.backend.dtos.response.TagResDto;
import com.hrms.backend.services.TagService;
import com.hrms.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tags")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<TagResDto>>> getAllTags() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Tags fetched successfully", tagService.getAllTags()));
    }

    @PostMapping("/")
//    @PreAuthorize("hasAuthority('MANAGE_POST')")
    public ResponseEntity<ApiResponse<TagResDto>> createTag(@RequestParam String tagName) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Tag created successfully", tagService.createTag(tagName)));
    }
}
