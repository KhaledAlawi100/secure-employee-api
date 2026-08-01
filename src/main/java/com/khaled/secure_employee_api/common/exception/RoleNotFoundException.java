package com.khaled.secure_employee_api.common.exception;

import com.khaled.secure_employee_api.role.entity.RoleName;
import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends BaseException {
    public RoleNotFoundException(RoleName roleName) {
        super("Role not found: " + roleName, HttpStatus.NOT_FOUND);
    }
}
