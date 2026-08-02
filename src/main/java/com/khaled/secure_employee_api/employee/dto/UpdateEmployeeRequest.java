package com.khaled.secure_employee_api.employee.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateEmployeeRequest(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotNull
        Long departmentId,

        String phone,

        @NotNull
        Long positionId,


        Long managerId,

        @NotNull
        @Min(0)
        Double salary

) {
}