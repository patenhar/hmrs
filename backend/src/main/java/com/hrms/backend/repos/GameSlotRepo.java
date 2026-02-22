package com.hrms.backend.repos;

import com.hrms.backend.entities.GameSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GameSlotRepo extends JpaRepository<GameSlot, UUID> {
    @Query("""
SELECT gs FROM GameSlot gs
WHERE gs.game.pkGameId = :gameId
AND gs.date = :date
AND (
:date > CURRENT_DATE OR (
:date = CURRENT_DATE AND gs.beginTime > CURRENT_TIME
)
)
AND NOT EXISTS (
SELECT gb FROM GameBooking gb
WHERE gb.gameSlot.pkGameSlotId = gs.pkGameSlotId
)
ORDER BY gs.beginTime
""")
    List<GameSlot> findAvailableSlots(@Param("gameId") UUID gameId, @Param("date") LocalDate date);
}
