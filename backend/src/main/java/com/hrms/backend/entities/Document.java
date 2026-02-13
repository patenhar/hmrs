package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Documents")
@Getter @Setter
public class Document extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkDocumentId;

    @NotBlank
    private String accessUrl;

    @ManyToMany
    @JoinTable(
            name = "travel_documents",
            joinColumns = @JoinColumn(name = "fk_user_travel_id"),
            inverseJoinColumns = @JoinColumn(name = "fk_document_id")
    )
    private List<UserTravel> userTravels = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "fk_document_type_id", referencedColumnName = "pkDocumentTypeId")
    private DocumentType documentType;
}
