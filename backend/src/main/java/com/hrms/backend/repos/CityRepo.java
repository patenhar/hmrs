package com.hrms.backend.repos;

import com.hrms.backend.entities.City;
import com.hrms.backend.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CityRepo extends JpaRepository<City, UUID>{
    List<City> findCitiesByCityNameContainingIgnoreCaseAndCountry_PkCountryId(String cityName, UUID countryPkCountryId);
    Optional<City> findFirstByCityNameAndCountry(String cityName, Country country);
}
