package com.hrms.backend.repos;

import com.hrms.backend.entities.Travel;
import com.hrms.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TravelRepo extends JpaRepository<Travel, UUID>{

}
