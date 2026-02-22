package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Travels")
@Getter @Setter
public class Travel extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkTravelId;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private LocalDate travelDate;

    @NotNull
    private LocalDate returnDate;

    @NotNull
    private double maxGrantPerDay;

    @Email
    private String hrMail;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(
            name = "travel_destinations",
            joinColumns = @JoinColumn(name = "fk_travel_id"),
            inverseJoinColumns = @JoinColumn(name = "fk_address_id")
    )
    private List<Address> destinations = new ArrayList<>();

    @OneToMany(mappedBy = "travel")
    private List<UserTravel> userTravels = new ArrayList<>();
}
