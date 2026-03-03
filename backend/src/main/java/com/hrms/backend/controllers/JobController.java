package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.JobReqDto;
//import com.hrms.backend.dtos.response.JobResDto;
import com.hrms.backend.dtos.response.JobResDto;
import com.hrms.backend.entities.Job;
import com.hrms.backend.services.JobService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/jobs")
public class JobController {
    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('VIEW_JOB') or hasAuthority('MANAGE_ALL_JOB')")
    public ResponseEntity<ApiResponse<List<JobResDto>>> getAllJobs() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All jobs fetched successfully", jobService.getAllJobs()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_JOB') or hasAuthority('MANAGE_ALL_JOB')")
    public ResponseEntity<ApiResponse<JobResDto>> getJobById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Job fetched successfully", jobService.getJobById(id)));
    }

    @PostMapping(path = "/", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('MANAGE_ALL_JOB')")
    public ResponseEntity<ApiResponse<Job>> addJob(@Validated(OnCreate.class) @ModelAttribute JobReqDto jobReqDto) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Job added successfully", jobService.addJob(jobReqDto)));
    }

    @PatchMapping(path = "/{id}", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('MANAGE_ALL_JOB')")
    public ResponseEntity<ApiResponse<Job>> updateJob(@PathVariable UUID id, @Validated(OnUpdate.class) @ModelAttribute JobReqDto jobReqDto) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Job updated successfully", jobService.updateJob(id, jobReqDto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_ALL_JOB')")
    public ResponseEntity<ApiResponse<String>> deleteJob(@PathVariable UUID id) {
        String res = "Job could not be deleted";
        if (jobService.softDeleteJob(id)) {
            res = "Job deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null));
    }
}
