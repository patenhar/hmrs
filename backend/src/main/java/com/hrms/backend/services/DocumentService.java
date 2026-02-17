package com.hrms.backend.services;

import com.hrms.backend.dtos.request.DocumentReqDto;
import com.hrms.backend.dtos.request.TravelDocumentReqDto;
import com.hrms.backend.entities.Document;
import com.hrms.backend.entities.DocumentType;
import com.hrms.backend.repos.DocumentRepo;
import com.hrms.backend.repos.UserTravelRepo;
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
public class DocumentService implements IDocumentService {

    private final ModelMapper modelMapper;
    private final UserTravelRepo userTravelRepo;
    private final DocumentRepo documentRepo;
    private final S3Service s3Service;
    private final DocumentTypeService documentTypeService;

    public DocumentService(ModelMapper modelMapper, UserTravelRepo userTravelRepo, DocumentRepo documentRepo, S3Service s3Service, DocumentTypeService documentTypeService) {
        this.modelMapper = modelMapper;
        this.userTravelRepo = userTravelRepo;
        this.documentRepo = documentRepo;
        this.s3Service = s3Service;
        this.documentTypeService = documentTypeService;
    }

    public ApiResponse<Document> uploadTravelDocument(TravelDocumentReqDto travelDocumentReqDto) {
        MultipartFile file = travelDocumentReqDto.getFile();
        // Upload logic;
        String accessUrl = "";
        Document document = modelMapper.map(travelDocumentReqDto, Document.class);
        document.setAccessUrl(accessUrl);
        document.getUserTravels().add(userTravelRepo.findById(travelDocumentReqDto.getUserTravelId()).orElseThrow(() -> new ResourceNotFoundException("User travel record not found")));
        Document doc = documentRepo.save(document);
        return new ApiResponse<>("Document uploaded successfully", doc);
    }

    @Transactional
    public Document uploadDocument(DocumentReqDto documentReqDto) throws IOException {
        String accessUrl = s3Service.uploadFile(documentReqDto.getFile());

        Document document = new Document();
        document.setDocumentType(modelMapper.map(documentTypeService.findDocumentTypeById(documentReqDto.getFkDocumentTypeId()), DocumentType.class));
        document.setAccessUrl(accessUrl);

        return documentRepo.save(document);
    }

    public ApiResponse<List<Document>> getUserTravelDocument(UUID id) {
        return new ApiResponse<>("User travel documents fetched successfully", documentRepo.findDocumentsByUserTravel(id));
    }
}
