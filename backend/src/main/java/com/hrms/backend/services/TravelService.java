package com.hrms.backend.services;

import com.hrms.backend.dtos.request.AddressReqDto;
import com.hrms.backend.dtos.request.TravelInDto;
import com.hrms.backend.dtos.request.TravelReqDto;
import com.hrms.backend.dtos.response.TravelResDto;
import com.hrms.backend.dtos.spec.SortDto;
import com.hrms.backend.dtos.spec.TravelSpecDto;
import com.hrms.backend.entities.*;
import com.hrms.backend.repos.*;
import com.hrms.backend.utils.JsonStringToSortDto;
import com.hrms.backend.utils.ResourceNotFoundException;
import com.hrms.backend.utils.TravelSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

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

    public TravelService(TravelRepo travelRepo, ModelMapper modelMapper, AddressService addressService,
                         UserTravelService userTravelService, EmailService emailService,
                         UserService userService, DateService dateService,
                         NotificationService notificationService) {
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
        return travelRepo.findAll().stream()
                .filter(t -> !Boolean.TRUE.equals(t.getIsDeleted()))
                .map(t -> modelMapper.map(t, TravelResDto.class)).toList();
    }

    public Page<TravelResDto> searchTravelWithPaginationSortingAndFiltering(TravelInDto travelInDto) {
        TravelSpecDto travelSpecDto = modelMapper.map(travelInDto, TravelSpecDto.class);

        List<SortDto> sortDtos = JsonStringToSortDto.jsonStringToSortDto(travelInDto.getSort());
        List<Sort.Order> orders = new ArrayList<>();

        if (sortDtos != null) {
            for(SortDto sortDto: sortDtos) {
                Sort.Direction direction = Objects.equals(sortDto.getDirection(), "desc")
                        ? Sort.Direction.DESC : Sort.Direction.ASC;
                orders.add(new Sort.Order(direction,sortDto.getField()));
            }
        }

        PageRequest pageRequest = PageRequest.of(
                travelInDto.getPage(),
                travelInDto.getSize(),
                Sort.by(orders)
        );

        Specification<Travel> specification = TravelSpecification.getSpecification(travelSpecDto);
        Page<Travel> travels = travelRepo.findAll(specification, pageRequest);

        return travels.map(t -> modelMapper.map(t, TravelResDto.class));
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

    @Transactional
    public TravelResDto updateTravel(UUID id, TravelReqDto travelReqDto) {
        Travel travel = travelRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Travel not found"));
        travel.setTitle(travelReqDto.getTitle());
        travel.setDescription(travelReqDto.getDescription());
        travel.setHrMail(travelReqDto.getHrMail());
        travel.setMaxGrantPerDay(travelReqDto.getMaxGrantPerDay());
        travel.setTravelDate(dateService.convertToLocalDate(travelReqDto.getTravelDate()));
        travel.setReturnDate(dateService.convertToLocalDate(travelReqDto.getReturnDate()));

        travel.getDestinations().clear();
        if (travelReqDto.getDestinations() != null) {
            for (AddressReqDto addressReqDto : travelReqDto.getDestinations()) {
                travel.getDestinations().add(modelMapper.map(addressService.addAddress(addressReqDto), Address.class));
            }
        }

        travel.getUserTravels().clear();
        Travel saved = travelRepo.save(travel);
        if (travelReqDto.getUserIds() != null) {
            for (UUID userId : travelReqDto.getUserIds()) {
                userTravelService.saveUserTravel(userId, saved);
            }
        }
        return modelMapper.map(saved, TravelResDto.class);
    }

    @Transactional
    public boolean deleteTravel(UUID id) {
        Travel travel = travelRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Travel not found"));
        travel.setIsDeleted(true);
        travelRepo.save(travel);
        return true;
    }
}
