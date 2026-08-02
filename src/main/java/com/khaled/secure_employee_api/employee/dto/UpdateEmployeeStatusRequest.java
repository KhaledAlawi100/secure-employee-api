package com.khaled.secure_employee_api.employee.dto;

import com.khaled.secure_employee_api.employee.model.EmployeeStatus;

public record UpdateEmployeeStatusRequest(

        EmployeeStatus status

) {
}
