package com.khaled.secure_employee_api.position.exception;

import com.khaled.secure_employee_api.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class PositionAlreadyExistsException extends BaseException {

    public PositionAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}