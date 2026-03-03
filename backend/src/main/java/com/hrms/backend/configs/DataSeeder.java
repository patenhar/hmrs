package com.hrms.backend.configs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.backend.entities.*;
import com.hrms.backend.enums.ProfileStatus;
import com.hrms.backend.repos.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Slf4j
@Component
public class DataSeeder implements ApplicationRunner {

    private final CountryRepo    countryRepo;
    private final CityRepo        cityRepo;
    private final DepartmentRepo  departmentRepo;
    private final UserRepo        userRepo;
    private final ProfileRepo     profileRepo;
    private final RoleRepo        roleRepo;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(CountryRepo countryRepo, CityRepo cityRepo,
                      DepartmentRepo departmentRepo,
                      UserRepo userRepo, ProfileRepo profileRepo,
                      RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.countryRepo     = countryRepo;
        this.cityRepo        = cityRepo;
        this.departmentRepo  = departmentRepo;
        this.userRepo        = userRepo;
        this.profileRepo     = profileRepo;
        this.roleRepo        = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
//        seedCountriesAndCities();
//        seedProfiles();
    }

    private User createUserIfAbsent(String email, String rawPassword, String roleName) {
        return userRepo.findByEmail(email).orElseGet(() -> {
            Role role = roleRepo.findFirstByRoleName(roleName).orElseGet(() -> {
                Role r = new Role();
                r.setRoleName(roleName);
                return roleRepo.save(r);
            });
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setRole(role);
            return userRepo.save(user);
        });
    }

    private Profile createProfileIfAbsent(User user, String name, LocalDate birthDate,
                                          LocalDate joiningDate, String departmentName,
                                          Profile managerProfile) {
        Profile existing = profileRepo.findProfileByUser_PkUserId(user.getPkUserId());
        if (existing != null) return existing;

        Department dept = departmentRepo.findFirstByDepartmentName(departmentName)
                .orElseGet(() -> {
                    Department d = new Department();
                    d.setDepartmentName(departmentName);
                    return departmentRepo.save(d);
                });

        Profile profile = new Profile();
        profile.setName(name);
        profile.setBirthDate(birthDate);
        profile.setJoiningDate(joiningDate);
        profile.setUser(user);
        profile.setProfileStatus(ProfileStatus.ACTIVE);
        profile.setDepartment(dept);
        profile.setManagerProfile(managerProfile);
        return profileRepo.save(profile);
    }

    private void seedProfiles() {
        log.info("Seeding profiles...");

        // ── HR ───────────────────────────────────────────────────────────────
        User hrUser = createUserIfAbsent("harshpatel120605@gmail.com", "123456", "HR");
        Profile hrProfile = createProfileIfAbsent(hrUser, "Harsh Patel",
                LocalDate.of(1990, 6, 5), LocalDate.of(2020, 1, 1),
                "Human Resources", null);

        // ── Managers ─────────────────────────────────────────────────────────
        User manager1User = createUserIfAbsent("wolverine120605@gmail.com", "123456", "Manager");
        Profile manager1Profile = createProfileIfAbsent(manager1User, "James Logan",
                LocalDate.of(1985, 3, 12), LocalDate.of(2018, 6, 1),
                "Engineering", hrProfile);

        User manager2User = createUserIfAbsent("natasha.romanoff@hrms.com", "123456", "Manager");
        Profile manager2Profile = createProfileIfAbsent(manager2User, "Natasha Romanoff",
                LocalDate.of(1988, 11, 22), LocalDate.of(2019, 9, 1),
                "Product Management", hrProfile);

        User manager3User = createUserIfAbsent("tony.stark@hrms.com", "123456", "Manager");
        Profile manager3Profile = createProfileIfAbsent(manager3User, "Tony Stark",
                LocalDate.of(1983, 5, 29), LocalDate.of(2017, 1, 15),
                "Operations", hrProfile);

        // ── Employees ────────────────────────────────────────────────────────
        User emp1User = createUserIfAbsent("arthon120605@gmail.com", "123456", "Employee");
        createProfileIfAbsent(emp1User, "Arthon",
                LocalDate.of(1998, 6, 5), LocalDate.of(2023, 7, 1),
                "Engineering", manager1Profile);

        User emp2User = createUserIfAbsent("peter.parker@hrms.com", "123456", "Employee");
        createProfileIfAbsent(emp2User, "Peter Parker",
                LocalDate.of(2000, 8, 10), LocalDate.of(2024, 1, 15),
                "Engineering", manager1Profile);

        User emp3User = createUserIfAbsent("bruce.banner@hrms.com", "123456", "Employee");
        createProfileIfAbsent(emp3User, "Bruce Banner",
                LocalDate.of(1995, 12, 18), LocalDate.of(2022, 4, 1),
                "Engineering", manager1Profile);

        User emp4User = createUserIfAbsent("wanda.maximoff@hrms.com", "123456", "Employee");
        createProfileIfAbsent(emp4User, "Wanda Maximoff",
                LocalDate.of(1997, 2, 14), LocalDate.of(2023, 3, 20),
                "Product Management", manager2Profile);

        User emp5User = createUserIfAbsent("sam.wilson@hrms.com", "123456", "Employee");
        createProfileIfAbsent(emp5User, "Sam Wilson",
                LocalDate.of(1993, 9, 23), LocalDate.of(2021, 11, 1),
                "Product Management", manager2Profile);

        User emp6User = createUserIfAbsent("scott.lang@hrms.com", "123456", "Employee");
        createProfileIfAbsent(emp6User, "Scott Lang",
                LocalDate.of(1991, 4, 7), LocalDate.of(2022, 8, 10),
                "Operations", manager3Profile);

        log.info("Profiles seeding complete.");
    }

    private static final Set<String> TARGET_COUNTRIES = Set.of(
            "India", "United States", "Finland", "Australia", "United Kingdom"
    );

    private void seedCountriesAndCities() {
        if (countryRepo.count() > 0) {
            log.info("Countries/Cities already seeded - skipping.");
            return;
        }
        log.info("Fetching countries and cities from external API...");
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://countriesnow.space/api/v0.1/countries"))
                    .timeout(Duration.ofSeconds(30))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            if (root.path("error").asBoolean(true)) {
                log.warn("Countries API returned error - skipping seed.");
                return;
            }

            int countriesSeeded = 0;
            int citiesSeeded = 0;

            for (JsonNode countryNode : root.path("data")) {
                String countryName = countryNode.path("country").asText();
                if (!TARGET_COUNTRIES.contains(countryName)) continue;

                Country country = new Country();
                country.setCountryName(countryName);
                country = countryRepo.save(country);
                countriesSeeded++;

                for (JsonNode cityNode : countryNode.path("cities")) {
                    String cityName = cityNode.asText();
                    if (cityName.isBlank()) continue;
                    City city = new City();
                    city.setCityName(cityName);
                    city.setCountry(country);
                    cityRepo.save(city);
                    citiesSeeded++;
                }
            }
            log.info("Countries/Cities seeding complete: {} countries, {} cities seeded.", countriesSeeded, citiesSeeded);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Country/city seeding interrupted: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Failed to seed countries and cities: {}", e.getMessage(), e);
        }
    }
}
