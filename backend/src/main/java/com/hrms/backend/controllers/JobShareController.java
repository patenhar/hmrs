package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.JobShareReqDto;
import com.hrms.backend.entities.JobShareRecord;
import com.hrms.backend.services.JobShareRecordService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping("api/jobs")
public class JobShareController {
    private final JobShareRecordService jobShareRecordService;

    @Autowired
    public JobShareController(JobShareRecordService jobShareRecordService) {
        this.jobShareRecordService = jobShareRecordService;
    }

    @PostMapping("/{id}/share")
    @PreAuthorize("hasAuthority('SHARE_JOB') or hasAuthority('MANAGE_ALL_JOB')")
    public ResponseEntity<ApiResponse<String>> shareJob(@PathVariable UUID id, @RequestBody @Validated(OnCreate.class) JobShareReqDto jobShareRecordReqDto) throws MessagingException, IOException {
        jobShareRecordService.shareJob(jobShareRecordReqDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Job shared successfully", null));
    }

}
