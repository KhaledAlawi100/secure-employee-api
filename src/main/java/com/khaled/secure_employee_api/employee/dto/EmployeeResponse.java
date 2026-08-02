package com.khaled.secure_employee_api.employee.dto;

import com.khaled.secure_employee_api.department.dto.DepartmentSummaryResponse;
import com.khaled.secure_employee_api.employee.model.EmployeeStatus;
import com.khaled.secure_employee_api.position.dto.PositionSummaryResponse;

import java.time.LocalDate;

public record EmployeeResponse(

        Long id,

        String username,

        String email,

        String firstName,

        String lastName,

        String phone,

        DepartmentSummaryResponse department,

        PositionSummaryResponse position,

        ManagerSummaryResponse manager



) {
}