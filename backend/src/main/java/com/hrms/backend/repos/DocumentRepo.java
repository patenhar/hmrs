package com.hrms.backend.repos;

import com.hrms.backend.entities.Document;
import com.hrms.backend.entities.UserTravel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRepo extends JpaRepository<Document, UUID>{
    @Query(value = "SELECT * " +
            "FROM documents d " +
            "INNER JOIN travel_documents td " +
            "ON d.pk_document_id = td.fk_document_id " +
            "WHERE td.pk_user_travel_id = :userTravelId", nativeQuery = true)
    List<Document> findDocumentsByUserTravel(@Param("userTravelId") UUID userTravelId);
}
