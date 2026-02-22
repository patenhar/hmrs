package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.JobStakeHolderTypeReqDto;
import com.hrms.backend.dtos.response.JobStakeHolderTypeResDto;
import com.hrms.backend.services.JobStakeHolderTypeService;
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
@RequestMapping("api/stakeholder-types")
public class JobStakeHolderTypeController {
    private final JobStakeHolderTypeService jobStakeHolderTypeService;

    @Autowired
    public JobStakeHolderTypeController(JobStakeHolderTypeService jobStakeHolderTypeService) {
        this.jobStakeHolderTypeService = jobStakeHolderTypeService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<JobStakeHolderTypeResDto>>> getAllJobStakeHolderTypes() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All JobStakeHolder types fetched successfully", jobStakeHolderTypeService.getAllJobStakeHolderTypes()));
    }

    @GetMapping("/search")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<JobStakeHolderTypeResDto>>> getAllJobStakeHolderTypesByName(@RequestParam String name) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All JobStakeHolder types fetched successfully", jobStakeHolderTypeService.getAllJobStakeHolderTypesByName(name)));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<JobStakeHolderTypeResDto>> getJobStakeHolderTypeById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("JobStakeHolder type fetched successfully", jobStakeHolderTypeService.getJobStakeHolderTypeById(id)));
    }

    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_ROLE')")
    public ResponseEntity<ApiResponse<JobStakeHolderTypeResDto>> addJobStakeHolderType(@RequestBody @Validated(OnCreate.class) JobStakeHolderTypeReqDto jobStakeHolderTypeReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("JobStakeHolder type added successfully", jobStakeHolderTypeService.addJobStakeHolderType(jobStakeHolderTypeReqDto)));
    }

    @PatchMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<JobStakeHolderTypeResDto>> updateJobStakeHolderType(@PathVariable UUID id, @RequestBody @Validated(OnUpdate.class) JobStakeHolderTypeReqDto jobStakeHolderTypeReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("JobStakeHolder type updated successfully", jobStakeHolderTypeService.updateJobStakeHolderType(id, jobStakeHolderTypeReqDto)));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<String>> deleteJobStakeHolderType(@PathVariable UUID id) {
        String res = "JobStakeHolder type could not be deleted";
        if (jobStakeHolderTypeService.deleteJobStakeHolderType(id)){
            res = "JobStakeHolder type deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null ));
    }
}
