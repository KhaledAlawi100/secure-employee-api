package com.khaled.secure_employee_api.security.auth.dto;

public record RegisterResponse(
        Long id,
        String username,
        String email
) {
}
