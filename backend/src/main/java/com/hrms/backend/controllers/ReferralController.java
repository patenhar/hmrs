package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.ReferralReqDto;
import com.hrms.backend.services.ReferralService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping("api/jobs")
public class ReferralController {
    private final ReferralService referralService;

    @Autowired
    public ReferralController(ReferralService referralService) {
        this.referralService = referralService;
    }

    @PostMapping(path = "/{id}/refer", consumes = "multipart/form-data")
//    @PreAuthorize("hasAuthority('ADD_ROLE')")
    public ResponseEntity<ApiResponse<String>> referJob(@PathVariable UUID id, @Validated(OnCreate.class) @ModelAttribute ReferralReqDto referralReqDto) throws IOException, MessagingException {
        referralService.referJob(id, referralReqDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Job referred successfully", null));
    }

}
