package com.hrms.backend.services;

import com.hrms.backend.dtos.request.DocumentReqDto;
import com.hrms.backend.dtos.request.TravelDocumentReqDto;
import com.hrms.backend.dtos.response.DocumentResDto;
import com.hrms.backend.dtos.response.TravelDocumentResDto;
import com.hrms.backend.entities.Document;
import com.hrms.backend.entities.Travel;
import com.hrms.backend.entities.TravelDocument;
import com.hrms.backend.entities.UserTravel;
import com.hrms.backend.repos.DocumentRepo;
import com.hrms.backend.repos.TravelDocumentRepo;
import com.hrms.backend.repos.UserTravelRepo;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class TravelDocumentService {
    private final ModelMapper modelMapper;
    private final DocumentService documentService;
    private final UserTravelService userTravelService;
    private final TravelDocumentRepo travelDocumentRepo;
    private final UserService userService;

    public TravelDocumentService(ModelMapper modelMapper, DocumentService documentService, UserTravelService userTravelService, TravelDocumentRepo travelDocumentRepo, UserService userService) {
        this.modelMapper = modelMapper;
        this.documentService = documentService;
        this.userTravelService = userTravelService;
        this.travelDocumentRepo = travelDocumentRepo;
        this.userService = userService;
    }

    public TravelDocumentResDto uploadTravelDocument(TravelDocumentReqDto travelDocumentReqDto) throws IOException {
        Document document = documentService.uploadDocument(travelDocumentReqDto.getDocumentReqDto());
        TravelDocument travelDocument = new TravelDocument();
        travelDocument.setDocument(document);
        travelDocument.setUploadedBy(userService.getAuthenticatedUser());
        travelDocument.setUserTravel(modelMapper.map(userTravelService.findUserTravelById(travelDocumentReqDto.getUserTravelId()), UserTravel.class));
        return modelMapper.map(travelDocumentRepo.save(travelDocument), TravelDocumentResDto.class);
    }

    public List<TravelDocumentResDto> getUserTravelDocument(UUID id) {
        return travelDocumentRepo.findTravelDocumentsByUserTravel_PkUserTravelId(id).stream().map(d -> modelMapper.map(d, TravelDocumentResDto.class)).toList();
}
    public void deleteTravelDocument(UUID id) {
        travelDocumentRepo.deleteById(id);
    }

}
