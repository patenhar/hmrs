package com.hrms.backend.services;

import com.hrms.backend.dtos.request.DocumentReqDto;
import com.hrms.backend.dtos.request.TravelDocumentReqDto;
import com.hrms.backend.entities.Document;
import com.hrms.backend.repos.DocumentRepo;
import com.hrms.backend.services.interfaces.IDocumentService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final ModelMapper modelMapper;
    private final DocumentRepo documentRepo;
    private final S3Service s3Service;

    public DocumentService(ModelMapper modelMapper, DocumentRepo documentRepo, S3Service s3Service) {
        this.modelMapper = modelMapper;
        this.documentRepo = documentRepo;
        this.s3Service = s3Service;
    }

    @Transactional
    public Document uploadDocument(DocumentReqDto documentReqDto) throws IOException {
        String accessUrl = s3Service.uploadFile(documentReqDto.getFile());

        Document document = new Document();
        document.setDocumentType(documentReqDto.getDocumentType());
        document.setAccessUrl(accessUrl);

        return documentRepo.save(document);
    }
}
