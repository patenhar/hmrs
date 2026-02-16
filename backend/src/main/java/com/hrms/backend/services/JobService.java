package com.hrms.backend.services;

import com.hrms.backend.dtos.request.JobReqDto;
import com.hrms.backend.dtos.request.JobShareReqDto;
import com.hrms.backend.dtos.request.ReferralReqDto;
import com.hrms.backend.entities.*;
import com.hrms.backend.repos.JobRepo;
import com.hrms.backend.services.interfaces.IJobService;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.UUID;

public class JobService implements IJobService {
    private final JobRepo jobRepo;
    private final ModelMapper mapper;
    private final EmailService emailService;
    private final UserService userService;
    JobService(JobRepo jobRepo, ModelMapper mapper, EmailService emailService, UserService userService) {
        this.jobRepo = jobRepo;
        this.mapper = mapper;
        this.emailService = emailService;
        this.userService = userService;
    }

    private Job findJobById(UUID id) {
        return jobRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Job not found"));
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepo.findAll();
    }

    @Override
    public Job addJob(JobReqDto jobReqDto) {
        return null;
    }

    @Override
    public Job shareJob(JobShareReqDto jobShareReqDto){
        Job job = findJobById(jobShareReqDto.getJobId());
        User user = userService.getAuthenticatedUser();
        emailService.sendMailWithAttachment(jobShareReqDto.getEmail(), "Job shared by" + user.getEmail(), job.getTitle() + "\n" + job.getDescription(), job.getJd().getAccessUrl());
        JobShareRecord jobShareRecord = mapper.map(jobShareReqDto, JobShareRecord.class);
        jobShareRecord.setUser(user);
//        jobShareRecordRepo.save(jobShareRecord);
        return job;
    }

    public Job referFriendToJob(ReferralReqDto referralReqDto) {
        Job job = findJobById(referralReqDto.getJobId());
        User user = userService.getAuthenticatedUser();
//        Document document = documentService.uploadDocument();
        Document document = new Document();
        for (JobStakeHolder jobStakeHolder: job.getJobStakeHolders()){
            emailService.sendMailWithAttachment(jobStakeHolder.getUser().getEmail(), "Job referral", "Details" , document.getAccessUrl());
        }
        Referral referral = mapper.map(referralReqDto, Referral.class);
        referral.setJob(job);
        referral.setUser(user);
//        referral.setCvUrl(document.getAccessUrl());
//        referral.setReferralStatus("");
//        referralRepo.save(referral);
        return new Job();
    }
}
