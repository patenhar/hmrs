package com.hrms.backend.services;

import com.hrms.backend.dtos.request.JobReqDto;
import com.hrms.backend.dtos.request.JobShareReqDto;
import com.hrms.backend.dtos.request.JobStakeHolderReqDto;
import com.hrms.backend.dtos.request.ReferralReqDto;
import com.hrms.backend.dtos.response.JobResDto;
import com.hrms.backend.entities.*;
import com.hrms.backend.repos.JobRepo;
import com.hrms.backend.services.interfaces.IJobService;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class JobService {
    private final JobRepo jobRepo;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private final UserService userService;
    private final DocumentService documentService;
    private final JobStakeHolderService jobStakeHolderService;

    public JobService(JobRepo jobRepo, ModelMapper modelMapper, EmailService emailService, UserService userService, DocumentService documentService, JobStakeHolderService jobStakeHolderService) {
        this.jobRepo = jobRepo;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
        this.userService = userService;
        this.documentService = documentService;
        this.jobStakeHolderService = jobStakeHolderService;
    }

    public Job findJobById(UUID id) {
        return jobRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Job not found"));
    }

    public List<JobResDto> getAllJobs() {
        return jobRepo.findAll().stream()
                .filter(job -> !Boolean.TRUE.equals(job.getIsDeleted()))
                .map(job -> modelMapper.map(job, JobResDto.class)).toList();
    }

    @Transactional
    public boolean softDeleteJob(UUID id) {
        Job job = findJobById(id);
        job.setIsDeleted(true);
        jobRepo.save(job);
        return true;
    }

    public JobResDto getJobById(UUID id) {
        return modelMapper.map( findJobById(id), JobResDto.class);
    }

    @Transactional
    public Job addJob(JobReqDto jobReqDto) throws IOException {
        Document jd = documentService.uploadDocument(jobReqDto.getDocumentReqDto());
        Job job = modelMapper.map(jobReqDto, Job.class);
        job.setJd(jd);
        Job newJob = jobRepo.save(job);
        for(JobStakeHolderReqDto jobStakeHolderReqDto: jobReqDto.getJobStakeHolderReqDtos()) {
            jobStakeHolderService.addJobStakeHolder(newJob, jobStakeHolderReqDto);
        }
        return newJob;
    }

    @Transactional
    public Job updateJob(UUID id, JobReqDto jobReqDto) throws IOException {
        Job job = findJobById(id);
        job.setTitle(jobReqDto.getTitle());
        job.setDescription(jobReqDto.getDescription());
        if (jobReqDto.getDocumentReqDto() != null && jobReqDto.getDocumentReqDto().getFile() != null && !jobReqDto.getDocumentReqDto().getFile().isEmpty()) {
            Document jd = documentService.uploadDocument(jobReqDto.getDocumentReqDto());
            job.setJd(jd);
        }
        job.getJobStakeHolders().clear();
        for(JobStakeHolderReqDto jobStakeHolderReqDto: jobReqDto.getJobStakeHolderReqDtos()) {
            jobStakeHolderService.addJobStakeHolder(job, jobStakeHolderReqDto);
        }
        return jobRepo.save(job);
    }
}
