package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.ProfileReqDto;
import com.hrms.backend.dtos.response.ProfileResDto;
import com.hrms.backend.entities.Profile;
import com.hrms.backend.services.ProfileService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/profiles")
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('VIEW_PROFILE') or hasAuthority('MANAGE_ALL_PROFILE')")
    public ResponseEntity<ApiResponse<List<ProfileResDto>>> getAllProfiles() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All profiles fetched successfully", profileService.getAllProfiles()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_PROFILE') or hasAuthority('MANAGE_ALL_PROFILE')")
    public ResponseEntity<ApiResponse<ProfileResDto>> getProfileById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Profile fetched successfully", profileService.getProfileById(id)));
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('VIEW_PROFILE') or hasAuthority('MANAGE_ALL_PROFILE')")
    public ResponseEntity<ApiResponse<ProfileResDto>> getProfileByUserId(@PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Profile fetched successfully", profileService.getProfileByUserId(userId)));
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('MANAGE_ALL_PROFILE')")
    public ResponseEntity<ApiResponse<ProfileResDto>> addProfile(@RequestBody @Validated(OnCreate.class) ProfileReqDto profileReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Profile added successfully", profileService.addProfile(profileReqDto)));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_ALL_PROFILE') or hasAuthority('UPDATE_OWN_PROFILE')")
    public ResponseEntity<ApiResponse<ProfileResDto>> updateProfile(@PathVariable UUID id, @RequestBody @Validated(OnUpdate.class) ProfileReqDto profileReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Profile updated successfully", profileService.updateProfile(id, profileReqDto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_ALL_PROFILE')")
    public ResponseEntity<ApiResponse<String>> deleteProfile(@PathVariable UUID id) {
        String res = "Profile not deleted";
        if (profileService.deleteProfile(id)){
            res = "Profile deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null ));
    }
}
