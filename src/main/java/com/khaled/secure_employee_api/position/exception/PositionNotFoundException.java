package com.khaled.secure_employee_api.position.exception;

import com.khaled.secure_employee_api.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class PositionNotFoundException extends BaseException {

    public PositionNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}