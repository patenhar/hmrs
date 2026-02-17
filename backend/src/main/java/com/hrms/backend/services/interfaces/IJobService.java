package com.hrms.backend.services.interfaces;

import com.hrms.backend.dtos.request.JobReqDto;
import com.hrms.backend.dtos.request.JobShareReqDto;
import com.hrms.backend.entities.Job;

import java.io.IOException;
import java.util.List;

public interface IJobService {
    List<Job> getAllJobs();
    Job addJob(JobReqDto jobReqDto) throws IOException;
}
