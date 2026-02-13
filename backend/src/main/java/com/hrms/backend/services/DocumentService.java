package com.hrms.backend.services;

import com.hrms.backend.dtos.request.DocumentReqDto;
import com.hrms.backend.entities.Document;
import com.hrms.backend.repos.DocumentRepo;
import com.hrms.backend.repos.UserTravelRepo;
import com.hrms.backend.services.interfaces.IDocumentService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService implements IDocumentService {

    private final ModelMapper modelMapper;
    private final UserTravelRepo userTravelRepo;
    private final DocumentRepo documentRepo;

    public DocumentService(ModelMapper modelMapper, UserTravelRepo userTravelRepo, DocumentRepo documentRepo) {
        this.modelMapper = modelMapper;
        this.userTravelRepo = userTravelRepo;
        this.documentRepo = documentRepo;
    }

    public ApiResponse<Document> uploadDocument(DocumentReqDto documentReqDto) {
        MultipartFile file = documentReqDto.getFile();
        // Upload logic;
        String accessUrl = "";
        Document document = modelMapper.map(documentReqDto, Document.class);
        document.setAccessUrl(accessUrl);
        document.getUserTravels().add(userTravelRepo.findById(documentReqDto.getUserTravelId()).orElseThrow(() -> new ResourceNotFoundException("User travel record not found")));
        Document doc = documentRepo.save(document);
        return new ApiResponse<>("Document uploaded successfully", doc);
    }

    public ApiResponse<List<Document>> getUserTravelDocument(UUID id) {
        return new ApiResponse<>("User travel documents fetched successfully", documentRepo.findDocumentsByUserTravel(id));
    }
}
