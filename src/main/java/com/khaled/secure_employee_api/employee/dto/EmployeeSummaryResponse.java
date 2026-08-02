package com.khaled.secure_employee_api.employee.dto;

import com.khaled.secure_employee_api.department.dto.DepartmentSummaryResponse;
import com.khaled.secure_employee_api.position.dto.PositionSummaryResponse;

public record EmployeeSummaryResponse(

        Long id,

        String firstName,

        String lastName,

        DepartmentSummaryResponse department,

        PositionSummaryResponse position

) {
}