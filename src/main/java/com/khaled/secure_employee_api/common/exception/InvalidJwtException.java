package com.khaled.secure_employee_api.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidJwtException extends BaseException{

    public InvalidJwtException() {
        super("Invalid JWT token", HttpStatus.UNAUTHORIZED);
    }
}
