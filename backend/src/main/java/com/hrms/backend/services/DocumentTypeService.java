package com.hrms.backend.services;

import com.hrms.backend.dtos.request.DocumentTypeReqDto;
import com.hrms.backend.dtos.response.DocumentTypeResDto;
import com.hrms.backend.entities.DocumentType;
import com.hrms.backend.repos.DocumentTypeRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentTypeService {
    private final DocumentTypeRepo documentTypeRepo;
    private final ModelMapper modelMapper;

    public DocumentTypeService(DocumentTypeRepo documentTypeRepo, ModelMapper modelMapper) {
        this.documentTypeRepo = documentTypeRepo;
        this.modelMapper = modelMapper;
    }

    public DocumentTypeResDto findDocumentTypeById(UUID id) {
        DocumentType documentType = documentTypeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Document type is not found"));
        return modelMapper.map(documentType, DocumentTypeResDto.class);
    }

    public List<DocumentTypeResDto> getAllDocumentTypes() {
        return documentTypeRepo.findAll().stream().map(st -> modelMapper.map(st, DocumentTypeResDto.class)).toList();
    }

    public DocumentTypeResDto getDocumentTypeById(UUID id) {
        return findDocumentTypeById(id);
    }

    public DocumentTypeResDto addDocumentType(DocumentTypeReqDto documentTypeReqDto) {
        DocumentType documentType =  documentTypeRepo.save(modelMapper.map(documentTypeReqDto, DocumentType.class));
        return modelMapper.map(documentType, DocumentTypeResDto.class);
    }

    public DocumentTypeResDto updateDocumentType(UUID id, DocumentTypeReqDto documentTypeReqDto) {
        DocumentTypeResDto documentType = findDocumentTypeById(id);
        modelMapper.map(documentTypeReqDto, documentType);
        DocumentType updatedDocumentType =  documentTypeRepo.save(modelMapper.map(documentTypeReqDto, DocumentType.class));
        return modelMapper.map(updatedDocumentType, DocumentTypeResDto.class);
    }

    public Boolean deleteDocumentType(UUID id) {
        findDocumentTypeById(id);
        documentTypeRepo.deleteById(id);
        return true;
    }
}
