package com.khaled.secure_employee_api.position.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePositionRequest(

        @NotBlank
        @Size(max = 100)
        String name,

        @Size(max = 500)
        String description

) {
}