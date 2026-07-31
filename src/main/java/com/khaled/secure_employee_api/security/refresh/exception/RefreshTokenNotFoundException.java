package com.khaled.secure_employee_api.security.refresh.exception;

import com.khaled.secure_employee_api.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class RefreshTokenNotFoundException extends BaseException {
    public RefreshTokenNotFoundException() {
        super("Refresh token not found.", HttpStatus.UNAUTHORIZED);
    }

}
