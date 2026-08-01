package com.khaled.secure_employee_api.config;

import com.khaled.secure_employee_api.role.entity.Role;
import com.khaled.secure_employee_api.role.entity.RoleName;
import com.khaled.secure_employee_api.role.repository.RoleRepository;
import com.khaled.secure_employee_api.user.entity.AppUser;
import com.khaled.secure_employee_api.user.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserSeeder implements CommandLineRunner {

    private final AppUserRepository appUserRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AdminSeedProperties adminSeedProperties;

    @Override
    @Transactional
    public void run(String... args) {

        if (appUserRepository.existsByUsername(
                adminSeedProperties.getUsername()
        )) {
            log.info("Admin user already exists. Skipping admin seed.");
            return;
        }

        Role adminRole = roleRepository
                .findByName(RoleName.ADMIN)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "ADMIN role must exist before creating admin user."
                        )
                );

        AppUser adminUser = AppUser.builder()
                .username(adminSeedProperties.getUsername())
                .email(adminSeedProperties.getEmail())
                .password(
                        passwordEncoder.encode(
                                adminSeedProperties.getPassword()
                        )
                )
                .enabled(true)
                .build();

        adminUser.getRoles().add(adminRole);

        appUserRepository.save(adminUser);

        log.info(
                "Initial ADMIN user '{}' created successfully.",
                adminUser.getUsername()
        );
    }
}