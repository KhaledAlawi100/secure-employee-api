package com.khaled.secure_employee_api.config;

import com.khaled.secure_employee_api.permission.entity.Permission;
import com.khaled.secure_employee_api.permission.entity.PermissionName;
import com.khaled.secure_employee_api.permission.repository.PermissionRepository;
import com.khaled.secure_employee_api.role.entity.Role;
import com.khaled.secure_employee_api.role.entity.RoleName;
import com.khaled.secure_employee_api.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class RolePermissionSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public void run(String... args) {

        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseThrow(() ->
                        new IllegalStateException("ADMIN role not found"));

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() ->
                        new IllegalStateException("USER role not found"));

        // ADMIN receives every permission
        Set<Permission> adminPermissions =
                new HashSet<>(permissionRepository.findAll());

        // USER receives only EMPLOYEE_READ
        Permission employeeRead = permissionRepository
                .findByName(PermissionName.EMPLOYEE_READ)
                .orElseThrow(() ->
                        new IllegalStateException("EMPLOYEE_READ permission not found"));

        Set<Permission> userPermissions = new HashSet<>();
        userPermissions.add(employeeRead);

        boolean changed = false;

        if (!adminRole.getPermissions().equals(adminPermissions)) {
            adminRole.setPermissions(adminPermissions);
            changed = true;
        }

        if (!userRole.getPermissions().equals(userPermissions)) {
            userRole.setPermissions(userPermissions);
            changed = true;
        }

        if (changed) {
            log.info("Default role-permission mappings seeded successfully.");
        } else {
            log.info("Role-permission mappings are already up to date.");
        }
    }
}