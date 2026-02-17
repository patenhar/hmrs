package com.hrms.backend.services;

import com.hrms.backend.dtos.request.JobShareReqDto;
import com.hrms.backend.entities.Job;
import com.hrms.backend.entities.JobShareRecord;
import com.hrms.backend.entities.User;
import com.hrms.backend.repos.JobShareRecordRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class JobShareRecordService {
    private final JobService jobService;
    private final UserService userService;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    private final JobShareRecordRepo jobShareRecordRepo;

    public JobShareRecordService(JobService jobService, UserService userService, EmailService emailService, ModelMapper modelMapper, JobShareRecordRepo jobShareRecordRepo) {
        this.jobService = jobService;
        this.userService = userService;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
        this.jobShareRecordRepo = jobShareRecordRepo;
    }

    public Job shareJob(JobShareReqDto jobShareReqDto){
        Job job = jobService.findJobById(jobShareReqDto.getJobId());
        User user = userService.getAuthenticatedUser();
        emailService.sendMailWithAttachment(jobShareReqDto.getEmail(), "Job shared by" + user.getEmail(), job.getTitle() + "\n" + job.getDescription(), job.getJd().getAccessUrl());
        JobShareRecord jobShareRecord = modelMapper.map(jobShareReqDto, JobShareRecord.class);
        jobShareRecord.setUser(user);
        jobShareRecord.setJob(job);
        jobShareRecordRepo.save(jobShareRecord);
        return job;
    }
}
