package com.khaled.secure_employee_api.dto;

public record RegisterResponse(
        Long id,
        String username,
        String email
) {
}
