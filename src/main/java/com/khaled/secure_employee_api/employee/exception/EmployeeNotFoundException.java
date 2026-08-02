package com.khaled.secure_employee_api.employee.exception;

import com.khaled.secure_employee_api.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class EmployeeNotFoundException extends BaseException {
    public EmployeeNotFoundException(String message) {

        super(message, HttpStatus.NOT_FOUND);
    }
}
