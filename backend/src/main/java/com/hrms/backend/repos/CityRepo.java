package com.hrms.backend.repos;

import com.hrms.backend.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CityRepo extends JpaRepository<City, UUID>{

}
