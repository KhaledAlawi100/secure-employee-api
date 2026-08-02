package com.khaled.secure_employee_api.department.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateDepartmentRequest(

        @NotBlank
        @Size(max = 100)
        String name,

        @Size(max = 500)
        String description

) {
}