package com.hrms.backend.services;

import com.hrms.backend.dtos.request.GameBookingReqDto;
import com.hrms.backend.dtos.request.GameSlotReqDto;
import com.hrms.backend.dtos.response.GameBookingResDto;
import com.hrms.backend.dtos.response.GameResDto;
import com.hrms.backend.dtos.response.GameSlotResDto;
import com.hrms.backend.dtos.response.RankedBookingsResDto;
import com.hrms.backend.entities.*;
import com.hrms.backend.repos.GameBookingRepo;
import com.hrms.backend.repos.UserRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class GameBookingService {
    private final GameBookingRepo gameBookingRepo;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final UserRepo userRepo;
    private final GameService gameService;
    private final GameSlotService gameSlotService;
    private final GameBookingStatusService gameBookingStatusService;
    private final EmailService emailService;

    public GameBookingService(GameBookingRepo gameBookingRepo, ModelMapper modelMapper, UserService userService, UserRepo userRepo, GameService gameService, GameSlotService gameSlotService, GameBookingStatusService gameBookingStatusService, EmailService emailService) {
        this.gameBookingRepo = gameBookingRepo;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.userRepo = userRepo;
        this.gameService = gameService;
        this.gameSlotService = gameSlotService;
        this.gameBookingStatusService = gameBookingStatusService;
        this.emailService = emailService;
    }

    public GameBookingResDto findGameBookingById(UUID id) {
        GameBooking gameBooking = gameBookingRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("GameBooking not found"));
        return modelMapper.map(gameBooking, GameBookingResDto.class);
    }

    public List<GameBookingResDto> getAllGameBookings() {
        return gameBookingRepo.findAll().stream().map(st -> modelMapper.map(st, GameBookingResDto.class)).toList();
    }

    public GameBookingResDto getGameBookingById(UUID id) {
        return findGameBookingById(id);
    }

    public List<GameBookingResDto> getGameBookingByUser(UUID userId) {
        return gameBookingRepo.findBookingByUser(userId).stream().map(gb -> modelMapper.map(gb, GameBookingResDto.class)).toList();
    }

    public int personalPriority(UUID userId, UUID gameID){
//        return gameBookingRepo.getRecentSlots(userId, gameID);
        return 0;
    }

    public double teamPriority(List<User> users, UUID gameId){
        return users.stream().mapToInt(u -> personalPriority(u.getPkUserId(), gameId)).average().orElse(0);
    }

    public int difference(List<User> users, UUID gameId){
        List<Integer> values = users.stream().map(u -> personalPriority(u.getPkUserId(), gameId)).toList();
        return Collections.max(values) - Collections.min(values);
    }

    // All statuses
//    public GameBookingResDto addGameBooking(GameBookingReqDto gameBookingReqDto) throws Exception {
//        GameSlotResDto gameSlot =  gameSlotService.findGameSlotById(gameBookingReqDto.getGameSlotId());
//        List<GameBooking> bookingsOnTheSlotDay = gameBookingRepo.findGameBookingsByUserOnDay(gameBookingReqDto.getTeamMemberIds(), gameSlot.getDate(), UUID.fromString(""));
//        if (!bookingsOnTheSlotDay.isEmpty()) {
//            throw new Exception("One or more team members have an active booking on the slot day");
//        }
//        GameResDto game = gameService.findGameById(gameSlot.getGame().getPkGameId());
//        List<User> teamMembers = userRepo.findAllById(gameBookingReqDto.getTeamMemberIds());
//        List<GameBooking> gameBookings = gameBookingRepo.findGameBookingsByGameSlotGamePkGameIdAndGameSlotDateAfterAndTeamMembersContaining(game.getPkGameId(), gameSlot.getDate().minusDays(game.getBookingCycle()),teamMembers);
//        GameBooking gameBooking = modelMapper.map(gameBookingReqDto, GameBooking.class);
//        gameBooking.setBookedBy(userService.getAuthenticatedUser());
//        gameBooking.setTeamMembers(teamMembers);
//        String statusId = ""; // Pending
//        if (gameSlot.getDate().isEqual(LocalDate.now()) && LocalTime.now().isBefore(gameSlot.getBeginTime())) {
//            statusId = ""; //Confirmed
//        }
//        gameBooking.setInversePriority(gameBookings.size());
//        gameBooking.setGameBookingStatus(modelMapper.map(gameBookingStatusService.findGameBookingStatusById(UUID.fromString(statusId)), GameBookingStatus.class));
//        GameBookingResDto booking = modelMapper.map(gameBookingRepo.save(gameBooking), GameBookingResDto.class);
//        String sub = ""; // Pending
//        if(booking.getGameBookingStatus().getPkGameBookingStatusId().equals("")){
//            sub = ""; // Confirmed
//        }
//        for(User user: teamMembers) {
//            emailService.sendMail(user.getEmail(), sub, "");
//        }
//        return booking;
//    }

    @Transactional
    public GameBookingResDto addGameBooking(GameBookingReqDto gameBookingReqDto) throws Exception {
        GameSlotResDto gameSlot =  gameSlotService.findGameSlotById(gameBookingReqDto.getGameSlotId());
        User authenticatedUser = userService.getAuthenticatedUser();
        gameBookingReqDto.getTeamMemberIds().add(authenticatedUser.getPkUserId());
        List<GameBooking> bookingsOnTheSlotDay = gameBookingRepo.findGameBookingsByUserOnDay(gameBookingReqDto.getTeamMemberIds(), gameSlot.getDate(), UUID.fromString("e2362c33-075c-45ae-9a54-5173a44a6523"));
        if (!bookingsOnTheSlotDay.isEmpty()) {
            throw new Exception("One or more team members have an active booking on the slot day");
        }
        List<User> teamMembers = userRepo.findAllById(gameBookingReqDto.getTeamMemberIds());
        GameBooking gameBooking = modelMapper.map(gameBookingReqDto, GameBooking.class);
        gameBooking.setGameSlot(modelMapper.map(gameSlot, GameSlot.class));
        gameBooking.setBookedBy(authenticatedUser);
        gameBooking.setTeamMembers(teamMembers);
        String statusId = "f47f1934-573b-4b89-95db-244a4ac47480"; // Pending
        if (gameSlot.getDate().isEqual(LocalDate.now()) && LocalTime.now().isBefore(gameSlot.getBeginTime()) && LocalTime.now().isAfter(gameSlot.getBeginTime().minusMinutes(15))) {
            statusId = "2515961b-1af2-4afd-a19d-0d29971e5090"; //Confirmed
        }
        gameBooking.setInversePriority(0);
        gameBooking.setGameBookingStatus(modelMapper.map(gameBookingStatusService.findGameBookingStatusById(UUID.fromString(statusId)), GameBookingStatus.class));
        GameBookingResDto booking = modelMapper.map(gameBookingRepo.save(gameBooking), GameBookingResDto.class);
        String sub = "f47f1934-573b-4b89-95db-244a4ac47480"; // Pending
        if(booking.getGameBookingStatus().getPkGameBookingStatusId().equals("")){
            sub = "2515961b-1af2-4afd-a19d-0d29971e5090"; // Confirmed
        }
        for(User user: teamMembers) {
            emailService.sendMail(user.getEmail(), sub, "");
        }
        return booking;
    }

    @Async
    @Scheduled(cron = "0 1 0 * * MON-FRI")
    @Transactional
    public void cron_ConfirmBookings() {
        GameSlot gameSlot = new GameSlot();
        List<RankedBookingsResDto> gameBookings = gameBookingRepo.findAllPendingGameBookingsForUpcomingDays(UUID.fromString(""), 5);

//        List<GameBooking> queue = gameBookingRepo.FindAllBookingsForUpcomingSlot();
        List<GameBooking> queue = new ArrayList<>();
        Game game = gameSlot.getGame();
        int max = game.getMaxPlayers();

        queue.sort((t1, t2) -> {
            double a1 = teamPriority(t1.getTeamMembers(), game.getPkGameId());
            double a2 = teamPriority(t2.getTeamMembers(), game.getPkGameId());
            return Double.compare(a1, a2);
        });

        List<User> finalTeam = new ArrayList<>();
        for(GameBooking booking: queue) {
            List<User> teamMembers = booking.getTeamMembers();
            int difference = difference(teamMembers, game.getPkGameId());
            if(difference <= 5) {
                if(teamMembers.size() <= max) {
                    finalTeam.addAll(teamMembers);
                    max -= teamMembers.size();
                }
            }
            else {
                List<User> teamSorted = teamMembers.stream().sorted((t1, t2) -> {
                    int p1 = personalPriority(t1.getPkUserId(), game.getPkGameId());
                    int p2 = personalPriority(t2.getPkUserId(), game.getPkGameId());
                    return Integer.compare(p1, p2);
                }).toList();

                for (User user: teamSorted) {
                    if (max <= 0) break;
                    finalTeam.add(user);
                    max--;
                }
            }
        }

//        if(!finalTeam.isEmpty()) {
//            GameBooking confirm = queue.get(0);
//            confirm.setTeamMembers(finalTeam);
//            confirm.setGameBookingStatus("");
//            for(int i = 1; i < queue.size(); i++) {
//                queue.get(i).setGameBookingStatus("");
//            }
//        }
        gameBookingRepo.saveAll(queue);
    }
}
