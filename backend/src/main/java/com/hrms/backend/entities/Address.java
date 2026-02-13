package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Addresses")
@Getter @Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkAddressId;

    @NotBlank
    private String addressLine1;
    private String addressLine2;

    @ManyToOne()
    @JoinColumn(name = "fk_city_id", referencedColumnName = "pkCityId")
    private City city;

    @ManyToMany(mappedBy = "destinations")
    private List<Travel> travels = new ArrayList<>();
}
