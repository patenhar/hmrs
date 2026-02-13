package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Cities")
@Getter @Setter
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkCityId;

    @NotBlank
    private String cityName;

    @ManyToOne()
    @JoinColumn(name = "fk_country_id", referencedColumnName = "pkCountryId")
    private Country country;

    @OneToMany(mappedBy = "city")
    private List<Address> addresses;
}
