package com.khaled.secure_employee_api.role.repository;


import com.khaled.secure_employee_api.permission.entity.PermissionName;
import com.khaled.secure_employee_api.role.entity.Role;
import com.khaled.secure_employee_api.role.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
