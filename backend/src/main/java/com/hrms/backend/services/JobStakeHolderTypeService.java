package com.hrms.backend.services;

import com.hrms.backend.dtos.request.JobStakeHolderReqDto;
import com.hrms.backend.dtos.request.PermissionDto;
import com.hrms.backend.dtos.response.JobStakeHolderTypeResDto;
import com.hrms.backend.entities.JobStakeHolderType;
import com.hrms.backend.entities.Permission;
import com.hrms.backend.repos.JobStakeHolderTypeRepo;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JobStakeHolderTypeService {
    private final JobStakeHolderTypeRepo jobStakeHolderTypeRepo;
    private final ModelMapper modelMapper;

    public JobStakeHolderTypeService(JobStakeHolderTypeRepo jobStakeHolderTypeRepo, ModelMapper modelMapper) {
        this.jobStakeHolderTypeRepo = jobStakeHolderTypeRepo;
        this.modelMapper = modelMapper;
    }

    public JobStakeHolderTypeResDto findJobStakeHolderTypeById(UUID id) {
        JobStakeHolderType jobStakeHolderType = jobStakeHolderTypeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Stakeholder type is not found"));
        return modelMapper.map(jobStakeHolderType, JobStakeHolderTypeResDto.class);
    }

    public List<JobStakeHolderTypeResDto> getAllJobStakeHolderTypes() {
        return jobStakeHolderTypeRepo.findAll().stream().map(st -> modelMapper.map(st, JobStakeHolderTypeResDto.class)).toList();
    }

    public JobStakeHolderTypeResDto getJobStakeHolderTypeById(UUID id) {
        return findJobStakeHolderTypeById(id);
    }

    public JobStakeHolderTypeResDto addJobStakeHolderType(JobStakeHolderReqDto jobStakeHolderReqDto) {
        JobStakeHolderType jobStakeHolderType =  jobStakeHolderTypeRepo.save(modelMapper.map(jobStakeHolderReqDto, JobStakeHolderType.class));
        return modelMapper.map(jobStakeHolderType, JobStakeHolderTypeResDto.class);
    }

    public JobStakeHolderTypeResDto updateJobStakeHolderType(UUID id, JobStakeHolderReqDto jobStakeHolderReqDto) {
        JobStakeHolderTypeResDto jobStakeHolderType = findJobStakeHolderTypeById(id);
        modelMapper.map(jobStakeHolderReqDto, jobStakeHolderType);
        JobStakeHolderType updatedJobStakeHolderType =  jobStakeHolderTypeRepo.save(modelMapper.map(jobStakeHolderReqDto, JobStakeHolderType.class));
        return modelMapper.map(updatedJobStakeHolderType, JobStakeHolderTypeResDto.class);
    }

    public Boolean deleteJobStakeHolderType(UUID id) {
        findJobStakeHolderTypeById(id);
        jobStakeHolderTypeRepo.deleteById(id);
        return true;
    }
}
