package com.khaled.secure_employee_api.exception;

import org.springframework.http.HttpStatus;

public class InvalidJwtException extends BaseException{

    public InvalidJwtException() {
        super("Invalid JWT token", HttpStatus.UNAUTHORIZED);
    }
}
