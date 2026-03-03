package com.hrms.backend.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "Games")
@Getter
@Setter
public class Game extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkGameId;

    @NotBlank
    private String gameName;

    @NotNull
    private int maxPlayers;

    @NotNull
    private Double duration;

    @NotNull
    @JdbcTypeCode(SqlTypes.TIME)
    private LocalTime operationHourBegin;

    @NotNull
    @JdbcTypeCode(SqlTypes.TIME)
    private LocalTime operationHourEnd;

    @NotNull
    private int bookingCycle;

    @NotNull
    private int version = 0;

    @ManyToMany(mappedBy = "games")
    private List<Profile> profiles = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameSlot> slots = new ArrayList<>();
}
