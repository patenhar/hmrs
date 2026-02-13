package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Countries")
@Getter @Setter
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkCountryId;

    @NotBlank
    private String countryName;

    @OneToMany(mappedBy = "country")
    private List<City> cities = new ArrayList<>();
}
