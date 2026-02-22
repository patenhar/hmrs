package com.hrms.backend.repos;

import com.hrms.backend.entities.Expense;
import com.hrms.backend.entities.Profile;
import com.hrms.backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProfileRepo extends JpaRepository<Profile, UUID>{
    @Query(value = "SELECT * FROM profiles WHERE fk_manager_id = :profileId" , nativeQuery = true)
    List<Profile> findDirectReports(@Param("profileId") UUID profileId);

    Profile findProfileByUser_PkUserId(UUID id);
}
