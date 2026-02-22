package com.hrms.backend.services;

import com.hrms.backend.dtos.request.CountryReqDto;
import com.hrms.backend.dtos.response.CountryResDto;
import com.hrms.backend.entities.Country;
import com.hrms.backend.repos.CountryRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CountryService {
    private final CountryRepo countryRepo;
    private final ModelMapper modelMapper;

    public CountryService(CountryRepo countryRepo, ModelMapper modelMapper) {
        this.countryRepo = countryRepo;
        this.modelMapper = modelMapper;
    }

    public CountryResDto findCountryById(UUID id) {
        Country country = countryRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense type is not found"));
        return modelMapper.map(country, CountryResDto.class);
    }

    public List<CountryResDto> getAllCountries() {
        return countryRepo.findAll().stream().map(st -> modelMapper.map(st, CountryResDto.class)).toList();
    }

    public List<CountryResDto> getCountriesByName(String name) {
        return countryRepo.findCountriesByCountryNameContainingIgnoreCase(name).stream().map(st -> modelMapper.map(st, CountryResDto.class)).toList();
    }

    public CountryResDto getCountryById(UUID id) {
        return findCountryById(id);
    }

    public CountryResDto addCountry(CountryReqDto countryReqDto) {
        Country country =  countryRepo.save(modelMapper.map(countryReqDto, Country.class));
        return modelMapper.map(country, CountryResDto.class);
    }
}
