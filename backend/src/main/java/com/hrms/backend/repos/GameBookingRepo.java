package com.hrms.backend.repos;

import com.hrms.backend.dtos.response.RankedBookingsResDto;
import com.hrms.backend.entities.GameBooking;
import com.hrms.backend.entities.User;
import com.hrms.backend.enums.GameBookingStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface GameBookingRepo extends JpaRepository<GameBooking, UUID>, JpaSpecificationExecutor<GameBooking> {

    List<GameBooking> findGameBookingsByGameSlotGamePkGameIdAndGameSlotDateAndGameSlotBeginTimeAfterOrderByGameSlotBeginTime(UUID gameSlotGamePkGameId, LocalDate gameSlotDate, LocalTime gameSlotBeginTime);

    List<GameBooking> findGameBookingsByGameSlotPkGameSlotId(UUID gameSlotPkGameSlotId);

    List<GameBooking> findGameBookingsByGameSlotGamePkGameIdAndGameSlotDateAfterAndTeamMembersContaining(UUID gameSlot_game_pkGameId, @NotNull LocalDate gameSlot_date, List<User> teamMembers);

    @Query(value = """
                SELECT ROW_NUMBER() OVER (PARTITION BY gs.date, gs.beginTime ORDER BY gb.inverse_priority, gb.created_at) as final_priority, *
                FROM game_bookings gb
                LEFT JOIN game_slots gs on gb.fk_game_slot_id = gs.pk_game_slot_id
                WHERE gb.game_booking_status = :gameBookingStatus
                AND (gs.date >= CAST(GETDATE() AS DATE)
                AND gs.date < DATEADD(DAY, :day, CAST(GETDATE() AS DATE)));
                """, nativeQuery = true)
    List<RankedBookingsResDto> findAllPendingGameBookingsForUpcomingDays(String gameBookingStatus, int day);

    @Query(value = """
SELECT gb
FROM GameBooking gb
JOIN gb.gameSlot gs
WHERE gs.date = :slotDate
AND gb.gameBookingStatus <> :cancelledStatus
AND EXISTS (
SELECT 1
FROM gb.teamMembers u
WHERE u.pkUserId IN :userIds
)
""")
    List<GameBooking> findGameBookingsByUserOnDay(List<UUID> userIds, LocalDate slotDate, GameBookingStatus cancelledStatus);

    List<GameBooking> findAllByGameBookingStatusAndGameSlotDateAndGameSlotBeginTime(GameBookingStatus gameBookingStatus, LocalDate date, LocalTime beginTime);

    @Query(value = """
SELECT gb
FROM GameBooking gb
JOIN gb.gameSlot gs
JOIN gs.game g
JOIN gb.teamMembers u
WHERE u.pkUserId = :userId
ORDER BY gs.date DESC
""")
    List<GameBooking> findBookingByUser(UUID userId);

    @Modifying
    @Query(value = "DELETE FROM booking_users WHERE fk_user_id = :userId", nativeQuery = true)
    void removeUserFromAllTeams(@Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE GameBooking gb SET gb.bookedBy = null WHERE gb.bookedBy.pkUserId = :userId")
    void clearBookedByUserId(@Param("userId") UUID userId);

    @Query("""
    SELECT COUNT(gb) FROM GameBooking gb
    JOIN gb.teamMembers u
    WHERE u.pkUserId = :userId
    AND gb.gameSlot.game.pkGameId = :gameId
    AND gb.gameBookingStatus = com.hrms.backend.enums.GameBookingStatus.CONFIRMED
    AND gb.gameSlot.date >= :since
    """)
    int countRecentConfirmedBookings(@Param("userId") UUID userId, @Param("gameId") UUID gameId, @Param("since") LocalDate since);
}
