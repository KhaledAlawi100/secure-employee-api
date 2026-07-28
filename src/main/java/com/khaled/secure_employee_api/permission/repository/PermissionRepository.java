package com.khaled.secure_employee_api.permission.repository;

import  org.springframework.data.jpa.repository.JpaRepository;
import com.khaled.secure_employee_api.permission.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByName(String name);
}
