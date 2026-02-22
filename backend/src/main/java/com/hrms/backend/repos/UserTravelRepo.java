package com.hrms.backend.repos;

import com.hrms.backend.dtos.response.UserTravelResDtoForTravel;
import com.hrms.backend.entities.UserTravel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserTravelRepo extends JpaRepository<UserTravel, UUID>{
    @Query(value = "SELECT * FROM UserTravels WHERE fk_user_id = :userId", nativeQuery = true)
    List<UserTravel> findTravelByUserId(@Param("userId") UUID userId);

    List<UserTravelResDtoForTravel> findUserTravelsByTravelPkTravelId(UUID travelPkTravelId);
}
