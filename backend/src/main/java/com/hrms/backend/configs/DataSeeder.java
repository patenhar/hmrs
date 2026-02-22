package com.hrms.backend.configs;

import com.hrms.backend.entities.Permission;
import com.hrms.backend.entities.Role;
import com.hrms.backend.entities.User;
import com.hrms.backend.repos.PermissionRepo;
import com.hrms.backend.repos.RoleRepo;
import com.hrms.backend.repos.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DataSeeder implements ApplicationRunner {

    private static final String ADMIN_EMAIL    = "admin@hrms.com";
    private static final String ADMIN_PASSWORD = "Admin@123";
    private static final String ADMIN_ROLE     = "Super Admin";

    private static final List<String> ALL_PERMISSIONS = List.of(
            // User & Profile
            "ADD_USER", "VIEW_USER", "UPDATE_USER", "MANAGE_USER",
            "ADD_PROFILE", "VIEW_PROFILE", "UPDATE_PROFILE", "MANAGE_PROFILE",
            "VIEW_ORGCHART",
            // Travel & Expenses
            "ADD_TRAVEL",   "VIEW_TRAVEL",   "UPDATE_TRAVEL",   "MANAGE_TRAVEL",
            "ADD_EXPENSE",  "VIEW_EXPENSE",  "UPDATE_EXPENSE",  "MANAGE_EXPENSE",
            "ADD_DOCUMENT", "VIEW_DOCUMENT",
            // Jobs & Referrals
            "ADD_JOB", "VIEW_JOB", "UPDATE_JOB", "MANAGE_JOB",
            "ADD_REFERRAL", "VIEW_REFERRAL",
            // Games / Bookings
            "ADD_BOOKING", "VIEW_BOOKINGS", "MANAGE_BOOKING",
            // Social
            "ADD_POST", "VIEW_POST", "UPDATE_POST", "DELETE_POST", "MANAGE_POST"
    );

    private final PermissionRepo permissionRepo;
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(PermissionRepo permissionRepo, RoleRepo roleRepo,
                      UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.permissionRepo = permissionRepo;
        this.roleRepo       = roleRepo;
        this.userRepo       = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        // 1. Ensure all permissions exist
        List<Permission> permissions = new ArrayList<>();
        for (String name : ALL_PERMISSIONS) {
            Permission perm = permissionRepo.findFirstByPermissionName(name)
                    .orElseGet(() -> {
                        Permission p = new Permission();
                        p.setPermissionName(name);
                        return permissionRepo.save(p);
                    });
            permissions.add(perm);
        }

        // 2. Ensure Super Admin role exists with all permissions
        Role adminRole = roleRepo.findFirstByRoleName(ADMIN_ROLE)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoleName(ADMIN_ROLE);
                    return roleRepo.save(r);
                });

        // Add any missing permissions to the role
        boolean updated = false;
        for (Permission perm : permissions) {
            boolean alreadyHas = adminRole.getPermissions().stream()
                    .anyMatch(p -> p.getPermissionName().equals(perm.getPermissionName()));
            if (!alreadyHas) {
                adminRole.getPermissions().add(perm);
                updated = true;
            }
        }
        if (updated) {
            roleRepo.save(adminRole);
        }

        // 3. Ensure admin user exists
        if (userRepo.findByEmail(ADMIN_EMAIL).isEmpty()) {
            User admin = new User();
            admin.setEmail(ADMIN_EMAIL);
            admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            admin.setRole(adminRole);
            userRepo.save(admin);
            log.info("==============================================");
            log.info("  Admin user seeded successfully");
            log.info("  Email   : {}", ADMIN_EMAIL);
            log.info("  Password: {}", ADMIN_PASSWORD);
            log.info("==============================================");
        } else {
            log.info("Admin user '{}' already exists — skipping seed.", ADMIN_EMAIL);
        }
    }
}
