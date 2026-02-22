package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Game_booking_statuses")
@Getter
@Setter
public class GameBookingStatus extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkGameBookingStatusId;

    @NotBlank
    private String gameBookingStatusName;

    @OneToMany(mappedBy = "gameBookingStatus")
    private List<GameBooking> gameBookings;
}
