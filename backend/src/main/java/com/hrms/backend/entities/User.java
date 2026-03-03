package com.hrms.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Users")
@Getter @Setter
public class User extends AuditableTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkUserId;

    @NotBlank
    @Column(unique = true, nullable = false, updatable = false)
    @Email
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @ManyToOne()
    @JoinColumn(name = "fk_role_id", referencedColumnName = "pkRoleId")
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTravel> userTravels = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Like> likes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Notification> notifications = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<JobStakeHolder> jobStakeHolders = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<JobShareRecord> jobShareRecords = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Referral> referrals = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "uploadedBy")
    private List<TravelDocument> uploadedDocuments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "lastActionBy")
    private List<Expense> actedExpenses = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "bookedBy")
    private List<GameBooking> gameBookings = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "teamMembers")
    private List<GameBooking> teamBookings = new ArrayList<>();
}
