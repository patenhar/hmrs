package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.ReferralInDto;
import com.hrms.backend.dtos.request.ReferralReqDto;
import com.hrms.backend.dtos.response.ReferralResDto;
import com.hrms.backend.enums.ReferralStatus;
import com.hrms.backend.services.ReferralService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
public class ReferralController {
    private final ReferralService referralService;

    @Autowired
    public ReferralController(ReferralService referralService) {
        this.referralService = referralService;
    }

    @GetMapping("/referrals")
    @PreAuthorize("hasAuthority('REFER_JOB') or hasAuthority('MANAGE_ALL_REFERRAL')")
    public ResponseEntity<ApiResponse<List<ReferralResDto>>> getAllReferrals() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All referrals fetched successfully", referralService.getAllReferrals()));
    }

    @GetMapping("/referrals/filtering&pagination&sorting")
    @PreAuthorize("hasAuthority('REFER_JOB') or hasAuthority('MANAGE_ALL_REFERRAL')")
    public ResponseEntity<Page<ReferralResDto>> getReferralsPaginated(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sort", defaultValue = "[{\"field\":\"name\",\"direction\":\"asc\"}]") String sort,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "job-title", required = false) String jobTitle,
            @RequestParam(name = "status", required = false) String status
    ) {
        Page<ReferralResDto> referrals = referralService.searchReferralsWithPaginationSortingAndFiltering(
                ReferralInDto.builder()
                        .name(normalizeOptionalString(name))
                        .email(normalizeOptionalString(email))
                        .jobTitle(normalizeOptionalString(jobTitle))
                        .status(normalizeOptionalString(status))
                        .page(page)
                        .size(size)
                        .sort(sort)
                        .build());
        return ResponseEntity.ok(referrals);
    }

    private String normalizeOptionalString(String value) {
        if (value == null) return null;
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    @PatchMapping("/referrals/{id}/status/{status}")
    @PreAuthorize("hasAuthority('MANAGE_ALL_REFERRAL')")
    public ResponseEntity<ApiResponse<ReferralResDto>> updateReferralStatus(@PathVariable UUID id, @PathVariable ReferralStatus status) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Referral status updated successfully", referralService.updateReferralStatus(id, status)));
    }

    @PostMapping(path = "/{id}/refer", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('REFER_JOB') or hasAuthority('MANAGE_ALL_REFERRAL')")
    public ResponseEntity<ApiResponse<String>> referJob(@PathVariable UUID id, @Validated(OnCreate.class) @ModelAttribute ReferralReqDto referralReqDto) throws IOException, MessagingException {
        referralService.referJob(id, referralReqDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Job referred successfully", null));
    }

}
