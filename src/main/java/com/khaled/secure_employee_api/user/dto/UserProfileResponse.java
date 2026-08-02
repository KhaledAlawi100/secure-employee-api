package com.khaled.secure_employee_api.user.dto;

import java.util.Set;

public record UserProfileResponse(

        Long id,

        String username,

        String email,

        boolean enabled,

        Set<String> roles

) {
}