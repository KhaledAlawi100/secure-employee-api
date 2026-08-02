package com.khaled.secure_employee_api.permission.repository;

import com.khaled.secure_employee_api.permission.entity.PermissionName;
import com.khaled.secure_employee_api.role.entity.RoleName;
import  org.springframework.data.jpa.repository.JpaRepository;
import com.khaled.secure_employee_api.permission.entity.Permission;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(PermissionName name);

    boolean existsByName(PermissionName permissionName);
}
