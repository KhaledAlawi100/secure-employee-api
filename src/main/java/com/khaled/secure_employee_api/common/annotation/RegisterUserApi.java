package com.khaled.secure_employee_api.common.annotation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Register a new user",
        description = "Creates a new user account and securely hashes the password using BCrypt."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "User registered successfully"
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid registration data"
        ),
        @ApiResponse(
                responseCode = "409",
                description = "Username or email already exists"
        )
})
public @interface RegisterUserApi {
}