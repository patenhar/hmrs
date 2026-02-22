package com.hrms.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "Travel_documents")
public class TravelDocument extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkTravelDocumentId;

    @ManyToOne
    @JoinColumn(name = "fk_user_travel_id", referencedColumnName = "pkUserTravelId")
    private UserTravel userTravel;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pkUserId")
    private User uploadedBy;

    @ManyToOne
    @JoinColumn(name = "fk_document_id", referencedColumnName = "pkDocumentId")
    private Document document;
}
