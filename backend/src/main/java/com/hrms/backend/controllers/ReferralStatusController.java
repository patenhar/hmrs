package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.ReferralStatusReqDto;
import com.hrms.backend.dtos.response.ReferralStatusResDto;
import com.hrms.backend.entities.ReferralStatus;
import com.hrms.backend.services.ReferralStatusService;
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
@RequestMapping("api/referral-statuses")
public class ReferralStatusController {
    private final ReferralStatusService referralStatusService;

    @Autowired
    public ReferralStatusController(ReferralStatusService referralStatusService) {
        this.referralStatusService = referralStatusService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<ReferralStatusResDto>>> getAllReferralStatuses() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All referral statuses fetched successfully", referralStatusService.getAllReferralStatuses()));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<ReferralStatusResDto>> getReferralStatusById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Referral Status fetched successfully", referralStatusService.getReferralStatusById(id)));
    }

    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_ROLE')")
    public ResponseEntity<ApiResponse<ReferralStatusResDto>> addReferralStatus(@RequestBody @Validated(OnCreate.class) ReferralStatusReqDto referralStatusReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Referral Status added successfully", referralStatusService.addReferralStatus(referralStatusReqDto)));
    }

    @PatchMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<ReferralStatusResDto>> updateReferralStatus(@PathVariable UUID id, @RequestBody @Validated(OnUpdate.class) ReferralStatusReqDto referralStatusReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Referral Status updated successfully", referralStatusService.updateReferralStatus(id, referralStatusReqDto)));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<String>> deleteReferralStatus(@PathVariable UUID id) {
        String res = "Referral Status could not be deleted";
        if (referralStatusService.deleteReferralStatus(id)){
            res = "Referral Status deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null ));
    }
}
