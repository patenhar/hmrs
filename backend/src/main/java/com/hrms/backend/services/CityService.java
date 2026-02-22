package com.hrms.backend.services;

import com.hrms.backend.dtos.request.CityReqDto;
import com.hrms.backend.dtos.response.CityResDto;
import com.hrms.backend.entities.City;
import com.hrms.backend.entities.Country;
import com.hrms.backend.repos.CityRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CityService {

    private final CityRepo cityRepo;
    private final ModelMapper modelMapper;
    private final CountryService countryService;

    public CityService(CityRepo cityRepo, ModelMapper modelMapper, CountryService countryService) {
        this.cityRepo = cityRepo;
        this.modelMapper = modelMapper;
        this.countryService = countryService;
    }

    public CityResDto findCityById(UUID id) {
        City city = cityRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense status not found"));
        return modelMapper.map(city, CityResDto.class);
    }

    public List<CityResDto> getAllCities() {
        return cityRepo.findAll().stream().map(st -> modelMapper.map(st, CityResDto.class)).toList();
    }

    public List<CityResDto> findCitiesByCountryId(String name, UUID id) {
        return cityRepo.findCitiesByCityNameContainingIgnoreCaseAndCountry_PkCountryId(name, id).stream().map(st -> modelMapper.map(st, CityResDto.class)).toList();
    }

    public CityResDto getCityById(UUID id) {
        return findCityById(id);
    }

    public CityResDto addCity(CityReqDto cityReqDto) {
        City city = new City();
        city.setCityName(cityReqDto.getCityName());
        city.setCountry(modelMapper.map(countryService.getCountryById(cityReqDto.getCountryId()), Country.class));
        return modelMapper.map(cityRepo.save(city), CityResDto.class);
    }
}
