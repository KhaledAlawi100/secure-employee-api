package com.khaled.secure_employee_api.exception;

import org.springframework.http.HttpStatus;

public class ExpiredJwtTokenException extends BaseException {

    public ExpiredJwtTokenException() {
        super("JWT token has expired", HttpStatus.UNAUTHORIZED);
    }
}
