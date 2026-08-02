package com.khaled.secure_employee_api.department.mapper;

import com.khaled.secure_employee_api.department.dto.CreateDepartmentRequest;
import com.khaled.secure_employee_api.department.dto.DepartmentResponse;
import com.khaled.secure_employee_api.department.dto.UpdateDepartmentRequest;
import com.khaled.secure_employee_api.department.entity.Department;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    Department toEntity(CreateDepartmentRequest request);

    DepartmentResponse toResponse(Department department);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDepartmentFromRequest(
            UpdateDepartmentRequest request,
            @MappingTarget Department department
    );
}