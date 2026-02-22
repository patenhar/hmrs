package com.hrms.backend.repos;

import com.hrms.backend.dtos.response.TravelDocumentResDto;
import com.hrms.backend.entities.Document;
import com.hrms.backend.entities.Travel;
import com.hrms.backend.entities.TravelDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TravelDocumentRepo extends JpaRepository<TravelDocument, UUID> {
    @Query(value = "SELECT * " +
            "FROM documents d " +
            "INNER JOIN travel_documents td " +
            "ON d.pk_document_id = td.fk_document_id " +
            "WHERE td.pk_user_travel_id = :userTravelId", nativeQuery = true)
    List<Document> findDocumentsByUserTravel(@Param("userTravelId") UUID userTravelId);

    List<TravelDocument> findTravelDocumentsByUserTravel_PkUserTravelId(UUID userTravelPkUserTravelId);
    void deleteAllByPkTravelDocumentId(UUID pkTravelDocumentId);
}
