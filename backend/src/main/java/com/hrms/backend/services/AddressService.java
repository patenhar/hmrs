package com.hrms.backend.services;

import com.hrms.backend.dtos.request.AddressReqDto;
import com.hrms.backend.dtos.response.AddressResDto;
import com.hrms.backend.dtos.response.CityResDto;
import com.hrms.backend.entities.Address;
import com.hrms.backend.entities.City;
import com.hrms.backend.entities.Country;
import com.hrms.backend.repos.AddressRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AddressService {
    private final AddressRepo addressRepo;
    private final ModelMapper modelMapper;
    private final CityService cityService;

    public AddressService(AddressRepo addressRepo, ModelMapper modelMapper, CityService cityService) {
        this.addressRepo = addressRepo;
        this.modelMapper = modelMapper;
        this.cityService = cityService;
    }

    public AddressResDto findAddressById(UUID id) {
        Address address = addressRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        return modelMapper.map(address, AddressResDto.class);
    }

    public List<AddressResDto> getAllAddresses() {
        return addressRepo.findAll().stream().map(st -> modelMapper.map(st, AddressResDto.class)).toList();
    }

    public AddressResDto getAddressById(UUID id) {
        return findAddressById(id);
    }

    public AddressResDto addAddress(AddressReqDto addressReqDto) {
        Address address = modelMapper.map(addressReqDto, Address.class);
        address.setCity(modelMapper.map(cityService.getCityById(addressReqDto.getCityId()),City.class));
        return modelMapper.map(addressRepo.save(address), AddressResDto.class);
    }

    public AddressResDto updateAddress(UUID id, AddressReqDto addressReqDto) {
        AddressResDto address = findAddressById(id);
        modelMapper.map(addressReqDto, address);
        Address updatedAddress =  addressRepo.save(modelMapper.map(addressReqDto, Address.class));
        return modelMapper.map(updatedAddress, AddressResDto.class);
    }

    public boolean deleteAddress(UUID id) {
        findAddressById(id);
        addressRepo.deleteById(id);
        return true;
    }
}
