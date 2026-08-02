package com.khaled.secure_employee_api.employee.exception;

import com.khaled.secure_employee_api.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class EmployeeAlreadyExistsException extends BaseException {
    public EmployeeAlreadyExistsException(String message) {

        super(message, HttpStatus.CONFLICT);
    }
}
