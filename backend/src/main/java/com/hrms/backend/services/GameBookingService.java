package com.hrms.backend.services;

import com.hrms.backend.dtos.request.GameBookingInDto;
import com.hrms.backend.dtos.request.GameBookingReqDto;
import com.hrms.backend.dtos.response.GameBookingResDto;
import com.hrms.backend.dtos.response.GameResDto;
import com.hrms.backend.dtos.response.GameSlotResDto;
import com.hrms.backend.dtos.spec.GameBookingSpecDto;
import com.hrms.backend.dtos.spec.SortDto;
import com.hrms.backend.entities.*;
import com.hrms.backend.enums.GameBookingStatus;
import com.hrms.backend.repos.GameBookingRepo;
import com.hrms.backend.repos.UserRepo;
import com.hrms.backend.utils.GameBookingSpecification;
import com.hrms.backend.utils.JsonStringToSortDto;
import com.hrms.backend.utils.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GameBookingService {
    private static final int FAIRNESS_THRESHOLD = 5;

    private final GameBookingRepo gameBookingRepo;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final UserRepo userRepo;
    private final GameService gameService;
    private final GameSlotService gameSlotService;
    private final EmailService emailService;
    private final NotificationService notificationService;

    public GameBookingService(GameBookingRepo gameBookingRepo, ModelMapper modelMapper, UserService userService, UserRepo userRepo, GameService gameService, GameSlotService gameSlotService, EmailService emailService, NotificationService notificationService) {
        this.gameBookingRepo = gameBookingRepo;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.userRepo = userRepo;
        this.gameService = gameService;
        this.gameSlotService = gameSlotService;
        this.emailService = emailService;
        this.notificationService = notificationService;
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

    public List<GameBookingResDto> getBookingsByGameDateAndTime(UUID gameId, LocalDate date, LocalTime time) {
        return gameBookingRepo.findGameBookingsByGameSlotGamePkGameIdAndGameSlotDateAndGameSlotBeginTimeAfterOrderByGameSlotBeginTime(gameId, date, time).stream().map(gb -> modelMapper.map(gb, GameBookingResDto.class)).toList();
    }

    public void cancelBooking(UUID bookingId) {
        GameBooking booking = gameBookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("GameBooking not found"));
        booking.setGameBookingStatus(GameBookingStatus.CANCELLED);
        gameBookingRepo.save(booking);

        String gameName = booking.getGameSlot() != null && booking.getGameSlot().getGame() != null
                ? booking.getGameSlot().getGame().getGameName() : "Game";
        String date = booking.getGameSlot() != null ? booking.getGameSlot().getDate().toString() : "";
        String time = booking.getGameSlot() != null ? booking.getGameSlot().getBeginTime().toString() : "";
        String title = "Booking Cancelled";
        String desc = "Your booking for " + gameName + " on " + date + " at " + time + " has been cancelled.";
        for (User member : booking.getTeamMembers()) {
            notificationService.createNotification(title, desc, member.getPkUserId());
            emailService.sendMail(member.getEmail(), title, desc);
        }
    }

    public Page<GameBookingResDto> searchGameBookingsWithPaginationSortingAndFiltering(GameBookingInDto dto) {
        LocalDate dateFrom = null;
        LocalDate dateTo = null;
        try {
            if (dto.getDateFrom() != null && !dto.getDateFrom().isBlank()) {
                dateFrom = LocalDate.parse(dto.getDateFrom());
            }
            if (dto.getDateTo() != null && !dto.getDateTo().isBlank()) {
                dateTo = LocalDate.parse(dto.getDateTo());
            }
        } catch (DateTimeParseException ignored) {}

        GameBookingSpecDto specDto = GameBookingSpecDto.builder()
                .userId(dto.getUserId())
                .gameName(dto.getGameName())
                .statusName(dto.getStatusName())
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();

        List<SortDto> sortDtos = JsonStringToSortDto.jsonStringToSortDto(dto.getSort());
        List<Sort.Order> orders = new ArrayList<>();
        if (sortDtos != null) {
            for (SortDto sortDto : sortDtos) {
                Sort.Direction direction = Objects.equals(sortDto.getDirection(), "desc")
                        ? Sort.Direction.DESC : Sort.Direction.ASC;
                orders.add(new Sort.Order(direction, mapSortField(sortDto.getField())));
            }
        }

        PageRequest pageRequest = PageRequest.of(
                dto.getPage(),
                dto.getSize(),
                orders.isEmpty() ? Sort.unsorted() : Sort.by(orders)
        );

        Specification<GameBooking> specification = GameBookingSpecification.getSpecification(specDto);
        Page<GameBooking> bookings = gameBookingRepo.findAll(specification, pageRequest);
        return bookings.map(gb -> modelMapper.map(gb, GameBookingResDto.class));
    }

    private String mapSortField(String field) {
        return switch (field) {
            case "statusName" -> "gameBookingStatus";
            case "gameName"   -> "gameSlot.game.gameName";
            case "date"       -> "gameSlot.date";
            case "beginTime"  -> "gameSlot.beginTime";
            case "endTime"    -> "gameSlot.endTime";
            case "bookedBy"   -> "bookedBy.email";
            default -> field;
        };
    }

    public int personalPriority(UUID userId, UUID gameId, LocalDate since){
        return gameBookingRepo.countRecentConfirmedBookings(userId, gameId, since);
    }

    public double teamPriority(List<User> users, UUID gameId, LocalDate since){
        return users.stream().mapToInt(u -> personalPriority(u.getPkUserId(), gameId, since)).average().orElse(0);
    }

    public int difference(List<User> users, UUID gameId, LocalDate since){
        if (users == null || users.size() < 2) {
            return 0;
        }
        List<Integer> values = users.stream().map(u -> personalPriority(u.getPkUserId(), gameId, since)).toList();
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
    public void addAutoBooking(GameSlot slot, List<User> teamMembers) {
        if (slot == null || teamMembers == null || teamMembers.isEmpty()) {
            return;
        }

        GameBooking gameBooking = new GameBooking();
        gameBooking.setBookedBy(teamMembers.get(0));
        gameBooking.setTeamMembers(new ArrayList<>(teamMembers));
        gameBooking.setGameSlot(slot);
        gameBooking.setGameBookingStatus(GameBookingStatus.PENDING);
        gameBooking.setInversePriority(0);
        gameBookingRepo.save(gameBooking);
    }

    @Transactional
    public GameBookingResDto addGameBooking(GameBookingReqDto gameBookingReqDto) throws Exception {
        GameSlotResDto gameSlot = gameSlotService.findGameSlotById(gameBookingReqDto.getGameSlotId());
        User authenticatedUser = userService.getAuthenticatedUser();
        List<UUID> teamMemberIds = new ArrayList<>(gameBookingReqDto.getTeamMemberIds());
        if (!teamMemberIds.contains(authenticatedUser.getPkUserId())) {
            teamMemberIds.add(authenticatedUser.getPkUserId());
        }

        List<GameBooking> bookingsOnTheSlotDay = gameBookingRepo.findGameBookingsByUserOnDay(teamMemberIds, gameSlot.getDate(), GameBookingStatus.CANCELLED);
        if (!bookingsOnTheSlotDay.isEmpty()) {
            throw new Exception("One or more team members have an active booking on the slot day");
        }
        List<User> teamMembers = userRepo.findAllById(teamMemberIds);
        GameBooking gameBooking = modelMapper.map(gameBookingReqDto, GameBooking.class);
        gameBooking.setGameSlot(modelMapper.map(gameSlot, GameSlot.class));
        gameBooking.setBookedBy(authenticatedUser);
        gameBooking.setTeamMembers(teamMembers);
        GameBookingStatus status = GameBookingStatus.PENDING;
        if (gameSlot.getDate().isEqual(LocalDate.now()) && LocalTime.now().isBefore(gameSlot.getBeginTime()) && LocalTime.now().isAfter(gameSlot.getBeginTime().minusMinutes(15))) {
            status = GameBookingStatus.CONFIRMED;
        }
        gameBooking.setInversePriority(0);
        gameBooking.setGameBookingStatus(status);
        GameBookingResDto booking = modelMapper.map(gameBookingRepo.save(gameBooking), GameBookingResDto.class);
        String statusName = booking.getGameBookingStatus().getDisplayName();
        String slotDate = gameSlot.getDate().toString();
        String slotTime = gameSlot.getBeginTime().toString();
        String gameName = gameSlot.getGame() != null ? gameSlot.getGame().getGameName() : "Game";
        String subject = "Game Booking " + statusName + " \u2013 " + gameName;
        String body = "Your booking for " + gameName + " on " + slotDate + " at " + slotTime
                + " has been " + statusName.toLowerCase() + ".";
        for (User user : teamMembers) {
            notificationService.createNotification(subject, body, user.getPkUserId());
            emailService.sendMail(user.getEmail(), subject, body);
        }
        return booking;
    }

    @Scheduled(cron = "0 * * * * MON-FRI")
    @Transactional
    public void cron_ConfirmBookings() {
        GameBookingStatus pending = GameBookingStatus.PENDING;
        GameBookingStatus confirmed = GameBookingStatus.CONFIRMED;
        GameBookingStatus cancelled = GameBookingStatus.CANCELLED;

        List<GameResDto> games = gameService.getAllGames();

        for(GameResDto game: games) {
            LocalTime now = LocalTime.now().withSecond(0).withNano(0);
            LocalDate since = LocalDate.now().minusDays(game.getBookingCycle());
            GameSlot gameSlot = gameSlotService.getSlotByGameDateAndBeginTimeRange(
                    game.getPkGameId(), LocalDate.now(), now.plusMinutes(1), now.plusMinutes(16));

            if(gameSlot != null) {
                LocalTime slotTime = gameSlot.getBeginTime();
                List<GameBooking> queue = gameBookingRepo.findAllByGameBookingStatusAndGameSlotDateAndGameSlotBeginTime(pending, LocalDate.now(), slotTime)
                        .stream()
                        .filter(booking -> booking.getGameSlot() != null
                                && booking.getGameSlot().getPkGameSlotId().equals(gameSlot.getPkGameSlotId()))
                        .collect(Collectors.toCollection(ArrayList::new));

                if (queue.isEmpty()) {
                    continue;
                }

                class QueueTeam {
                    private final List<User> teamMembers;
                    private final GameBooking sourceBooking;

                    private QueueTeam(List<User> teamMembers, GameBooking sourceBooking) {
                        this.teamMembers = teamMembers;
                        this.sourceBooking = sourceBooking;
                    }
                }

                List<QueueTeam> processingQueue = queue.stream()
                        .map(booking -> new QueueTeam(new ArrayList<>(booking.getTeamMembers()), booking))
                        .collect(Collectors.toCollection(ArrayList::new));

                List<User> droppedPlayersPool = new ArrayList<>();
                QueueTeam winner = null;

                while (!processingQueue.isEmpty()) {
                    processingQueue.sort((t1, t2) -> Double.compare(teamPriority(t1.teamMembers, game.getPkGameId(), since), teamPriority(t2.teamMembers, game.getPkGameId(), since)));

                    QueueTeam current = processingQueue.remove(0);
                    boolean isLegit = current.teamMembers != null
                            && !current.teamMembers.isEmpty()
                            && current.teamMembers.size() <= game.getMaxPlayers()
                            && difference(current.teamMembers, game.getPkGameId(), since) <= FAIRNESS_THRESHOLD;

                    if (isLegit) {
                        winner = current;
                        break;
                    }

                    if (current.sourceBooking != null) {
                        current.sourceBooking.setGameBookingStatus(cancelled);
                    }

                    if (current.teamMembers != null && !current.teamMembers.isEmpty()) {
                        droppedPlayersPool.addAll(current.teamMembers);
                    }
                }

                Set<UUID> seenInPool = new HashSet<>();
                List<User> pool = droppedPlayersPool.stream()
                        .filter(u -> seenInPool.add(u.getPkUserId()))
                        .sorted((u1, u2) -> Integer.compare(
                                personalPriority(u1.getPkUserId(), game.getPkGameId(), since),
                                personalPriority(u2.getPkUserId(), game.getPkGameId(), since)
                        ))
                        .collect(Collectors.toCollection(ArrayList::new));

                int maxPlayers = game.getMaxPlayers();
                while (pool.size() >= maxPlayers) {
                    List<User> candidate = new ArrayList<>(pool.subList(0, maxPlayers));
                    if (difference(candidate, game.getPkGameId(), since) <= FAIRNESS_THRESHOLD) {
                        processingQueue.add(new QueueTeam(candidate, null));
                        pool.subList(0, maxPlayers).clear();
                    } else {
                        pool.remove(pool.size() - 1);
                    }
                }

                if (winner == null && !processingQueue.isEmpty()) {
                    while (!processingQueue.isEmpty()) {
                        processingQueue.sort((t1, t2) -> Double.compare(
                                teamPriority(t1.teamMembers, game.getPkGameId(), since),
                                teamPriority(t2.teamMembers, game.getPkGameId(), since)));

                        QueueTeam current = processingQueue.remove(0);
                        boolean isLegit = current.teamMembers != null
                                && !current.teamMembers.isEmpty()
                                && current.teamMembers.size() <= game.getMaxPlayers()
                                && difference(current.teamMembers, game.getPkGameId(), since) <= FAIRNESS_THRESHOLD;

                        if (isLegit) {
                            winner = current;
                            break;
                        }
                    }
                }

                List<List<User>> rolloverTeams = new ArrayList<>();

                if (winner != null) {
                    if (winner.sourceBooking != null) {
                        winner.sourceBooking.setTeamMembers(new ArrayList<>(winner.teamMembers));
                        winner.sourceBooking.setGameBookingStatus(confirmed);
                    } else {
                        GameBooking confirmedBooking = new GameBooking();
                        confirmedBooking.setGameSlot(gameSlot);
                        confirmedBooking.setBookedBy(winner.teamMembers.get(0));
                        confirmedBooking.setTeamMembers(new ArrayList<>(winner.teamMembers));
                        confirmedBooking.setInversePriority(0);
                        confirmedBooking.setGameBookingStatus(confirmed);
                        gameBookingRepo.save(confirmedBooking);
                    }
                }

                for (GameBooking booking : queue) {
                    if (winner != null && winner.sourceBooking != null
                            && booking.getPkGameBookingId().equals(winner.sourceBooking.getPkGameBookingId())) {
                        continue;
                    }
                    booking.setGameBookingStatus(cancelled);
                }

                Set<String> addedSignatures = new HashSet<>();

                for (QueueTeam remaining : processingQueue) {
                    if (remaining.teamMembers != null && !remaining.teamMembers.isEmpty()) {
                        String signature = remaining.teamMembers.stream()
                                .map(user -> user.getPkUserId().toString())
                                .sorted()
                                .collect(Collectors.joining("|"));
                        if (addedSignatures.add(signature)) {
                            rolloverTeams.add(new ArrayList<>(remaining.teamMembers));
                        }
                    }
                }

                gameBookingRepo.saveAll(queue);

                for (GameBooking processedBooking : queue) {
                    String bookingStatus = processedBooking.getGameBookingStatus().getDisplayName();
                    String notifTitle = "Booking " + bookingStatus + " – " + game.getGameName();
                    String notifDesc = "Your game booking for " + game.getGameName() + " on "
                            + processedBooking.getGameSlot().getDate() + " at "
                            + processedBooking.getGameSlot().getBeginTime()
                            + " has been " + bookingStatus.toLowerCase() + ".";
                    for (User member : processedBooking.getTeamMembers()) {
                        notificationService.createNotification(notifTitle, notifDesc, member.getPkUserId());
                        emailService.sendMail(member.getEmail(), notifTitle, notifDesc);
                    }
                }

                if (!rolloverTeams.isEmpty()) {
                    List<GameSlotResDto> upcomingSlots = gameSlotService.getSlotsByGameDateAndTime(game.getPkGameId(), LocalDate.now(), slotTime);
                GameSlot nextSlot = upcomingSlots.isEmpty() ? null : gameSlotService.getGameSlotEntityById(upcomingSlots.get(0).getPkGameSlotId());
                    if (nextSlot != null) {
                        rolloverTeams.forEach(team -> addAutoBooking(nextSlot, team));
                    } else {
                        log.info("No next slot available for game {} at {}. Rollover teams were not auto-booked.", game.getGameName(), slotTime);
                    }
                }

            }

                // List<User> finalTeam = new ArrayList<>();
                // for(GameBooking booking: queue) {
                //     List<User> teamMembers = booking.getTeamMembers();
                //     int difference = difference(teamMembers, game.getPkGameId());
                //     if(difference <= 5) {
                //         if(teamMembers.size() <= max) {
                //             finalTeam.addAll(teamMembers);
                //             max -= teamMembers.size();
                //         }
                //     }
                //     else {
                //         List<User> teamSorted = teamMembers.stream().sorted((t1, t2) -> {
                //             int p1 = personalPriority(t1.getPkUserId(), game.getPkGameId());
                //             int p2 = personalPriority(t2.getPkUserId(), game.getPkGameId());
                //             return Integer.compare(p1, p2);
                //         }).toList();

                //         for (User user: teamSorted) {
                //    if (max <= 0) break;
                //    if (personalPriority(user.getPkUserId(), game.getPkGameId()) < ){

                //    }
                //             finalTeam.add(user);
                //             max--;
                //         }
                //     }
                // }

            }

        }





    //     List<GameBooking> queue = gameBookingRepo
    //             .findAllByGameBookingStatusAndGameSlotDateAndGameSlotBeginTime(pending, LocalDate.now(), slotTime);

    //     if (queue.isEmpty()) return;

    //     Game game = queue.get(0).getGameSlot().getGame();
    //     int max = game.getMaxPlayers();

    //     queue.sort((t1, t2) -> {
    //         double a1 = teamPriority(t1.getTeamMembers(), game.getPkGameId());
    //         double a2 = teamPriority(t2.getTeamMembers(), game.getPkGameId());
    //         return Double.compare(a1, a2);
    //     });

    //     List<User> finalTeam = new ArrayList<>();
    //     for(GameBooking booking: queue) {
    //         List<User> teamMembers = booking.getTeamMembers();
    //         int difference = difference(teamMembers, game.getPkGameId());
    //         if(difference <= 5) {
    //             if(teamMembers.size() <= max) {
    //                 finalTeam.addAll(teamMembers);
    //                 max -= teamMembers.size();
    //             }
    //         }
    //         else {
    //             List<User> teamSorted = teamMembers.stream().sorted((t1, t2) -> {
    //                 int p1 = personalPriority(t1.getPkUserId(), game.getPkGameId());
    //                 int p2 = personalPriority(t2.getPkUserId(), game.getPkGameId());
    //                 return Integer.compare(p1, p2);
    //             }).toList();

    //             for (User user: teamSorted) {
    //                if (max <= 0) break;
    //                if (personalPriority(user.getPkUserId(), game.getPkGameId()) < ){

    //                }
    //                 finalTeam.add(user);
    //                 max--;
    //             }
    //         }
    //     }

    //    LocalTime nextSlotTime = slotTime.plusHours(1);
    //    GameSlot gameSlot = gameSlotService.getUpcomingGameSlot(nextSlotTime);

    //     if (!finalTeam.isEmpty()) {
    //         GameBooking confirm = queue.get(0);
    //         confirm.setTeamMembers(finalTeam);
    //         confirm.setGameBookingStatus(confirmed);
    //         for (int i = 1; i < queue.size(); i++) {
    //             queue.get(i).setGameBookingStatus(cancelled);
    //         }
    //     }
    //     gameBookingRepo.saveAll(queue);
    // }
}
