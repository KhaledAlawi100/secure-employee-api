package com.khaled.secure_employee_api.department.exception;

import com.khaled.secure_employee_api.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class DepartmentAlreadyExistsException extends BaseException {

    public DepartmentAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}