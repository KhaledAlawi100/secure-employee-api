package com.khaled.secure_employee_api.department.exception;

import com.khaled.secure_employee_api.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class DepartmentNotFoundException extends BaseException {

    public DepartmentNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}