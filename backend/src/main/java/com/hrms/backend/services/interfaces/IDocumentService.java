package com.hrms.backend.services.interfaces;

import com.hrms.backend.dtos.request.DocumentReqDto;
import com.hrms.backend.entities.Document;
import com.hrms.backend.utils.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface IDocumentService {
    ApiResponse<Document> uploadDocument(DocumentReqDto documentReqDto);
    ApiResponse<List<Document>> getUserTravelDocument(UUID id);
}
