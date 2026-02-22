package com.hrms.backend.services;

import com.hrms.backend.dtos.response.PostVisibilityResDto;
import com.hrms.backend.entities.PostVisibility;
import com.hrms.backend.repos.PostVisibilityRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostVisibilityService {
    private final PostVisibilityRepo postVisibilityRepo;
    private final ModelMapper modelMapper;

    public PostVisibilityService(PostVisibilityRepo postVisibilityRepo, ModelMapper modelMapper) {
        this.postVisibilityRepo = postVisibilityRepo;
        this.modelMapper = modelMapper;
    }

    public List<PostVisibilityResDto> getAllVisibilities() {
        return postVisibilityRepo.findAll().stream()
                .map(v -> modelMapper.map(v, PostVisibilityResDto.class))
                .toList();
    }

    public PostVisibility findEntityById(UUID id) {
        return postVisibilityRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post visibility not found"));
    }
}
