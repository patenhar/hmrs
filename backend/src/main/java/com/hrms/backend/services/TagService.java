package com.hrms.backend.services;

import com.hrms.backend.dtos.response.TagResDto;
import com.hrms.backend.entities.Tag;
import com.hrms.backend.repos.TagRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TagService {
    private final TagRepo tagRepo;
    private final ModelMapper modelMapper;

    public TagService(TagRepo tagRepo, ModelMapper modelMapper) {
        this.tagRepo = tagRepo;
        this.modelMapper = modelMapper;
    }

    public List<TagResDto> getAllTags() {
        return tagRepo.findAll().stream()
                .map(t -> modelMapper.map(t, TagResDto.class))
                .toList();
    }

    public TagResDto getTagById(UUID id) {
        Tag tag = tagRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        return modelMapper.map(tag, TagResDto.class);
    }

    public Tag findTagEntityById(UUID id) {
        return tagRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
    }

    public TagResDto createTag(String tagName) {
        Tag tag = new Tag();
        tag.setTag(tagName);
        return modelMapper.map(tagRepo.save(tag), TagResDto.class);
    }
}
