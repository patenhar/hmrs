package com.hrms.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "User_Travels")
@Getter @Setter
public class UserTravel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkUserTravelId;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pkUserId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "fk_travel_id", referencedColumnName = "pkTravelId")
    private Travel travel;

    @OneToMany(mappedBy = "userTravel")
    private List<Expense> expenses = new ArrayList<>();

    @ManyToMany(mappedBy = "userTravels")
    private List<Document> documents = new ArrayList<>();
}
