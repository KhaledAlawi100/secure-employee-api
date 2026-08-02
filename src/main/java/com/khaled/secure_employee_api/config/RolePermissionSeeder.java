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

        Set<Permission> adminPermissions =
                buildAdminPermissions();

        Set<Permission> userPermissions =
                buildUserPermissions();

        boolean changed = false;

        if (!adminRole.getPermissions().equals(adminPermissions)) {

            adminRole.setPermissions(adminPermissions);

            roleRepository.save(adminRole);

            changed = true;
        }

        if (!userRole.getPermissions().equals(userPermissions)) {

            userRole.setPermissions(userPermissions);

            roleRepository.save(userRole);

            changed = true;
        }

        if (changed) {

            log.info(
                    "Default role-permission mappings seeded successfully."
            );

        } else {

            log.info(
                    "Role-permission mappings are already up to date."
            );
        }
    }

    private Set<Permission> buildAdminPermissions() {

        return new HashSet<>(permissionRepository.findAll());
    }

    private Set<Permission> buildUserPermissions() {

        Set<Permission> permissions = new HashSet<>();

        permissions.add(getPermission(PermissionName.EMPLOYEE_READ));
        permissions.add(getPermission(PermissionName.DEPARTMENT_READ));
        permissions.add(getPermission(PermissionName.POSITION_READ));

        return permissions;
    }

    private Permission getPermission(PermissionName permissionName) {

        return permissionRepository.findByName(permissionName)
                .orElseThrow(() ->
                        new IllegalStateException(
                                permissionName + " permission not found."
                        )
                );
    }

}