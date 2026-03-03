package com.hrms.backend.entities;

import com.hrms.backend.enums.GameBookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "Game_bookings")
@Getter
@Setter
public class GameBooking extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkGameBookingId;

    @OneToOne
    @JoinColumn(name = "fk_game_slot_id", referencedColumnName = "pkGameSlotId")
    private GameSlot gameSlot;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pkUserId")
    private User bookedBy;

    @NotNull
    private long inversePriority;

    @Enumerated(EnumType.STRING)
    private GameBookingStatus gameBookingStatus;

    @ManyToMany
    @JoinTable(
            name = "booking_users",
            joinColumns = @JoinColumn(name = "fk_game_booking_id", referencedColumnName = "pkGameBookingId"),
            inverseJoinColumns = @JoinColumn(name = "fk_user_id", referencedColumnName = "pkUserId")
    )
    private List<User> teamMembers = new ArrayList<>();
}
