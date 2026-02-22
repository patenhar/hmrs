package com.hrms.backend.dtos.response;

import com.hrms.backend.entities.Document;
import com.hrms.backend.entities.UserTravel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class TravelDocumentResDto {
    private UUID pkTravelDocumentId;
    private UserTravelResDto userTravelResDto;
    private UserResDto uploadedBy;
    private DocumentResDto document;
    private LocalDate createAt;
}
