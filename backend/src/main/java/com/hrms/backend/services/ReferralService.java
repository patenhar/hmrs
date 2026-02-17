package com.hrms.backend.services;

import com.hrms.backend.dtos.request.ReferralReqDto;
import com.hrms.backend.entities.*;
import com.hrms.backend.repos.ReferralRepo;
import com.hrms.backend.repos.ReferralStatusRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ReferralService {

    private final JobService jobService;
    private final UserService userService;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    private final DocumentService documentService;
    private final ReferralRepo referralRepo;
    private final ReferralStatusService referralStatusService;

    public ReferralService(JobService jobService, UserService userService, EmailService emailService, ModelMapper modelMapper, DocumentService documentService, ReferralRepo referralRepo, ReferralStatusRepo referralStatusRepo, ReferralStatusService referralStatusService) {
        this.jobService = jobService;
        this.userService = userService;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
        this.documentService = documentService;
        this.referralRepo = referralRepo;
        this.referralStatusService = referralStatusService;
    }

    public Job referFriendToJob(ReferralReqDto referralReqDto) throws IOException {
        Job job = jobService.findJobById(referralReqDto.getJobId());
        User user = userService.getAuthenticatedUser();
        Document document = documentService.uploadDocument(referralReqDto.getDocumentReqDto());
        for (JobStakeHolder jobStakeHolder: job.getJobStakeHolders()){
            emailService.sendMailWithAttachment(jobStakeHolder.getUser().getEmail(), "Job referral", "Details" , document.getAccessUrl());
        }
        Referral referral = modelMapper.map(referralReqDto, Referral.class);
        referral.setJob(job);
        referral.setUser(user);
//        referral.setReferralStatus(referralStatusService.findReferralStatusById(""));
        referralRepo.save(referral);
        return job;
    }
}
