package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


@Entity
@Table(name = "Game_slots")
@Getter
@Setter
public class GameSlot extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkGameSlotId;

    @NotNull
    private LocalDate date;

    @NotNull
    @JdbcTypeCode(SqlTypes.TIME)
    private LocalTime beginTime;

    @NotNull
    @JdbcTypeCode(SqlTypes.TIME)
    private LocalTime endTime;

    private int version = 0;

    @ManyToOne
    @JoinColumn(name = "fk_game_id", referencedColumnName = "pkGameId")
    private Game game;

    @OneToOne(mappedBy = "gameSlot", cascade = CascadeType.ALL, orphanRemoval = true)
    private GameBooking booking;
}
