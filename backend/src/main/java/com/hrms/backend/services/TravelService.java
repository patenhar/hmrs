package com.hrms.backend.services;

import com.hrms.backend.dtos.request.AddressReqDto;
import com.hrms.backend.dtos.request.TravelReqDto;
import com.hrms.backend.dtos.request.UserTravelReqDto;
import com.hrms.backend.dtos.response.TravelResDto;
import com.hrms.backend.dtos.response.UserResDto;
import com.hrms.backend.entities.*;
import com.hrms.backend.repos.*;
import com.hrms.backend.services.interfaces.ITravelService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TravelService {
    private final TravelRepo travelRepo;
    private final ModelMapper modelMapper;
    private final AddressService addressService;
    private final UserTravelService userTravelService;
    private final EmailService emailService;
    private final UserService userService;
    private final DateService dateService;
    private final NotificationService notificationService;

    public TravelService(TravelRepo travelRepo, ModelMapper modelMapper, AddressService addressService, UserTravelService userTravelService, EmailService emailService, UserService userService, DateService dateService, NotificationService notificationService) {
        this.travelRepo = travelRepo;
        this.modelMapper = modelMapper;
        this.addressService = addressService;
        this.userTravelService = userTravelService;
        this.emailService = emailService;
        this.userService = userService;
        this.dateService = dateService;
        this.notificationService = notificationService;
    }

    public TravelResDto findTravelById(UUID id) {
        Travel travel = travelRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Travel not found"));
        return modelMapper.map(travel, TravelResDto.class);
    }

    public List<TravelResDto> getAllTravels() {
        return travelRepo.findAll().stream().map(t -> modelMapper.map(t, TravelResDto.class)).toList();
    }

    public TravelResDto getTravelById(UUID id) {
        return findTravelById(id);
    }

    public List<TravelResDto> getTravelByUserId(UUID id) {
        return  null;
    }

    @Transactional
    public boolean addTravel(TravelReqDto travelReqDto) {
        Travel travel = new Travel();
        travel.setTravelDate(dateService.convertToLocalDate(travelReqDto.getTravelDate()));
        travel.setReturnDate(dateService.convertToLocalDate(travelReqDto.getReturnDate()));
        travel.setTitle(travelReqDto.getTitle());
        travel.setDescription(travelReqDto.getDescription());
        travel.setHrMail(travelReqDto.getHrMail());
        travel.setMaxGrantPerDay(travelReqDto.getMaxGrantPerDay());

        List<AddressReqDto> addressReqDtos = travelReqDto.getDestinations();
        List<Address> addresses = new ArrayList<>();
        if (addressReqDtos != null){
            for(AddressReqDto addressReqDto: addressReqDtos){
                addresses.add(modelMapper.map((addressService.addAddress(addressReqDto)), Address.class));
            }
        }
        travel.getDestinations().addAll(addresses);
        Travel t = travelRepo.save(travel);
        for(UUID userId: travelReqDto.getUserIds()){
            userTravelService.saveUserTravel(userId, t);
        }
        String body = """
                Details:
                Title: %s
                Description: %s
                Date: %s - %s
                HR Mail: %s
                Grant limit: %s
                Destinations:
                %s
                Going with:
                %s
                """.formatted(t.getTitle(), t.getDescription(), t.getTravelDate(), t.getReturnDate(), t.getHrMail(), t.getMaxGrantPerDay(), t.getDestinations().stream().map(d -> "- " + d.getAddressLine1() + " " + d.getAddressLine2() + " " + d.getCity().getCityName() + d.getCity().getCountry().getCountryName() + "\n"), t.getUserTravels().stream().map(u -> u.getUser().getEmail()));
        for(UUID userId: travelReqDto.getUserIds()){

            emailService.sendMail(userService.findUserById(userId).getEmail(), "About new travel plan", "Congratulations, you are going on a trip!" + "Details:\n" + body);
            notificationService.createNotification("New Travel Plan", "You are going on a trip!", userId);
        }
        return true;
    }

    public TravelResDto updateTravel(UUID id, TravelReqDto roleDto) {
        return null;
    }

    public boolean deleteTravel(UUID id) {
        TravelResDto travelResDto = findTravelById(id);
        if (travelResDto.getTravelDate().isBefore(LocalDate.now())) {
            return false;
        }
        travelRepo.deleteById(id);
        return true;
    }
}
