package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.ProfileStatusReqDto;
import com.hrms.backend.dtos.response.ProfileStatusResDto;
import com.hrms.backend.entities.ProfileStatus;
import com.hrms.backend.services.ProfileStatusService;
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
@RequestMapping("api/profile-statuses")
public class ProfileStatusController {
    private final ProfileStatusService profileStatusService;

    @Autowired
    public ProfileStatusController(ProfileStatusService profileStatusService) {
        this.profileStatusService = profileStatusService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<ProfileStatusResDto>>> getAllProfileStatuses() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All profile statuses fetched successfully", profileStatusService.getAllProfileStatuses()));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<ProfileStatusResDto>> getProfileStatusById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Profile Status fetched successfully", profileStatusService.getProfileStatusById(id)));
    }

    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_ROLE')")
    public ResponseEntity<ApiResponse<ProfileStatusResDto>> addProfileStatus(@RequestBody @Validated(OnCreate.class) ProfileStatusReqDto profileStatusReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Profile Status added successfully", profileStatusService.addProfileStatus(profileStatusReqDto)));
    }

    @PatchMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<ProfileStatusResDto>> updateProfileStatus(@PathVariable UUID id, @RequestBody @Validated(OnUpdate.class) ProfileStatusReqDto profileStatusReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Profile Status updated successfully", profileStatusService.updateProfileStatus(id, profileStatusReqDto)));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<String>> deleteProfileStatus(@PathVariable UUID id) {
        String res = "Profile Status not deleted";
        if (profileStatusService.deleteProfileStatus(id)){
            res = "Profile Status deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null ));
    }
}
