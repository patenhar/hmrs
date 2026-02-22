package com.hrms.backend.repos;

import com.hrms.backend.dtos.response.RankedBookingsResDto;
import com.hrms.backend.entities.GameBooking;
import com.hrms.backend.entities.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface GameBookingRepo extends JpaRepository<GameBooking, UUID> {

    List<GameBooking> findGameBookingsByGameSlotGamePkGameIdAndGameSlotDateAfterAndTeamMembersContaining(UUID gameSlot_game_pkGameId, @NotNull LocalDate gameSlot_date, List<User> teamMembers);

    @Query(value = """
                SELECT ROW_NUMBER() OVER (PARTITION BY gs.date, gs.beginTime ORDER BY gb.inverse_priority, gb.created_at) as final_priority, *
                FROM game_bookings gb
                LEFT JOIN game_slots gs on gb.fk_game_slot_id = gs.pk_game_slot_id
                WHERE gb.fk_game_booking_status_id = :gameBookingStatusId 
                AND (gs.date >= CAST(GETDATE() AS DATE)
                AND gs.date < DATEADD(DAY, :day, CAST(GETDATE() AS DATE)));
                """, nativeQuery = true)
    List<RankedBookingsResDto> findAllPendingGameBookingsForUpcomingDays(UUID gameBookingStatusId, int day);

    @Query(value = """
SELECT gb
FROM GameBooking gb
JOIN gb.gameSlot gs
JOIN gb.gameBookingStatus bs
WHERE gs.date = :slotDate
AND bs.pkGameBookingStatusId <> :cancelledStatus
AND EXISTS (
SELECT 1
FROM gb.teamMembers u
WHERE u.pkUserId IN :userIds
)
""")
    List<GameBooking> findGameBookingsByUserOnDay(List<UUID> userIds, LocalDate slotDate, UUID cancelledStatus);

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
}
