package com.khaled.secure_employee_api.position.exception;

import com.khaled.secure_employee_api.common.exception.BaseException;
import org.springframework.http.HttpStatus;

// this exception is thrown when trying to delete a position that is still in use by an employee
public class PositionInUseException extends BaseException {

    public PositionInUseException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}