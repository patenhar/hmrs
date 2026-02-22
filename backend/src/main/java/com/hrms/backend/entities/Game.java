package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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
    private LocalTime operationHourBegin;

    @NotNull
    private LocalTime operationHourEnd;

    @NotNull
    private int bookingCycle;

    @ManyToMany(mappedBy = "games")
    private List<Profile> profiles = new ArrayList<>();
}
