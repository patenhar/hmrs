package com.hrms.backend.services;

import com.hrms.backend.dtos.request.JobStakeHolderReqDto;
import com.hrms.backend.entities.Job;
import com.hrms.backend.entities.JobStakeHolder;
import com.hrms.backend.repos.JobStakeHolderRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobStakeHolderService {
    private final JobStakeHolderRepo jobStakeHolderRepo;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public JobStakeHolderService(JobStakeHolderRepo jobStakeHolderRepo, UserService userService, ModelMapper modelMapper) {
        this.jobStakeHolderRepo = jobStakeHolderRepo;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public void addJobStakeHolder(Job job, JobStakeHolderReqDto jobStakeHolderReqDto) {
        JobStakeHolder jobStakeHolder = new JobStakeHolder();
        jobStakeHolder.setUser(userService.findById(jobStakeHolderReqDto.getUserId()));
        jobStakeHolder.setJob(job);
        jobStakeHolder.setJobStakeHolderType(jobStakeHolderReqDto.getJobStakeHolderType());
        jobStakeHolderRepo.save(jobStakeHolder);
    }

    @Transactional
    public void deleteAllByJob(Job job) {
        jobStakeHolderRepo.deleteAllByJob(job);
        jobStakeHolderRepo.flush();
    }
}
