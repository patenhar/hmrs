package com.hrms.backend.services;

import com.hrms.backend.dtos.request.JobStakeHolderReqDto;
import com.hrms.backend.dtos.response.UserResDto;
import com.hrms.backend.entities.Job;
import com.hrms.backend.entities.JobStakeHolder;
import com.hrms.backend.entities.JobStakeHolderType;
import com.hrms.backend.entities.User;
import com.hrms.backend.repos.JobStakeHolderRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class JobStakeHolderService {
    private final JobStakeHolderRepo jobStakeHolderRepo;
    private final UserService userService;
    private final JobStakeHolderTypeService jobStakeHolderTypeService;
    private final ModelMapper modelMapper;

    public JobStakeHolderService(JobStakeHolderRepo jobStakeHolderRepo, UserService userService, JobStakeHolderTypeService jobStakeHolderTypeService, ModelMapper modelMapper) {
        this.jobStakeHolderRepo = jobStakeHolderRepo;
        this.userService = userService;
        this.jobStakeHolderTypeService = jobStakeHolderTypeService;
        this.modelMapper = modelMapper;
    }

    public void addJobStakeHolder(Job job, JobStakeHolderReqDto jobStakeHolderReqDto){
        UserResDto user = userService.findUserById(jobStakeHolderReqDto.getUserId());
        JobStakeHolder jobStakeHolder = new JobStakeHolder();
        jobStakeHolder.setUser(modelMapper.map(user, User.class));
        jobStakeHolder.setJob(job);
        JobStakeHolderType jobStakeHolderType = modelMapper.map(jobStakeHolderTypeService.findJobStakeHolderTypeById(jobStakeHolderReqDto.getJobStakeHolderTypeId()), JobStakeHolderType.class);
        jobStakeHolder.setJobStakeHolderType(jobStakeHolderType);
        jobStakeHolderRepo.save(jobStakeHolder);
    }
}
