package com.khaled.secure_employee_api.config;

import com.khaled.secure_employee_api.permission.entity.Permission;
import com.khaled.secure_employee_api.permission.entity.PermissionName;
import com.khaled.secure_employee_api.permission.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class PermissionSeeder implements CommandLineRunner {

    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public void run(String... args) {

        int createdPermissions = 0;

        for (PermissionName permissionName : PermissionName.values()) {

            boolean exists =
                    permissionRepository.existsByName(permissionName);

            if (!exists) {

                Permission permission = Permission.builder()
                        .name(permissionName)
                        .build();

                permissionRepository.save(permission);

                createdPermissions++;
            }
        }

        if (createdPermissions > 0) {

            log.info(
                    "{} permission(s) seeded successfully.",
                    createdPermissions
            );

        } else {

            log.info("All permissions already exist.");
        }
    }

}