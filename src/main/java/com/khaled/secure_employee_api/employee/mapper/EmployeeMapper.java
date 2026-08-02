package com.khaled.secure_employee_api.employee.mapper;

import com.khaled.secure_employee_api.department.dto.DepartmentSummaryResponse;
import com.khaled.secure_employee_api.department.entity.Department;
import com.khaled.secure_employee_api.employee.dto.*;
import com.khaled.secure_employee_api.employee.model.Employee;
import com.khaled.secure_employee_api.position.dto.PositionSummaryResponse;
import com.khaled.secure_employee_api.position.entity.Position;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    // =====================================================
    // Entity Mapping
    // =====================================================

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "approved", ignore = true)
    @Mapping(target = "status", ignore = true)
    Employee toEntity(CreateEmployeeRequest request);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "approved", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEmployeeFromRequest(
            UpdateEmployeeRequest request,
            @MappingTarget Employee employee
    );

    // =====================================================
    // Admin Response
    // =====================================================

    @Mapping(target = "department", source = "department")
    @Mapping(target = "position", source = "position")
    @Mapping(target = "manager", source = "manager")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    EmployeeAdminResponse toAdminResponse(Employee employee);

    // =====================================================
    // Employee Profile Response
    // =====================================================

    @Mapping(target = "department", source = "department")
    @Mapping(target = "position", source = "position")
    @Mapping(target = "manager", source = "manager")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    EmployeeProfileResponse toProfileResponse(Employee employee);

    // =====================================================
    // Employee Response (General)
    // =====================================================

    @Mapping(target = "department", source = "department")
    @Mapping(target = "position", source = "position")
    @Mapping(target = "manager", source = "manager")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    EmployeeResponse toResponse(Employee employee);

    // =====================================================
    // Employee Summary
    // =====================================================

    EmployeeSummaryResponse toSummaryResponse(Employee employee);

    // =====================================================
    // Nested Objects
    // =====================================================

    DepartmentSummaryResponse toDepartmentSummary(Department department);

    PositionSummaryResponse toPositionSummary(Position position);

    ManagerSummaryResponse toManagerSummary(Employee manager);
}