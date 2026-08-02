package com.khaled.secure_employee_api.employee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateEmployeeRequest(

        @NotNull
        Long userId,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        String phone,

        @NotNull
        Long departmentId,

        Long managerId,


        @NotNull
        Long positionId,


        @NotNull
        @PositiveOrZero
        Double salary

) {
}