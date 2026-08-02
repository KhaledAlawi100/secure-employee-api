package com.khaled.secure_employee_api.department.service;

import com.khaled.secure_employee_api.department.dto.CreateDepartmentRequest;
import com.khaled.secure_employee_api.department.dto.DepartmentResponse;
import com.khaled.secure_employee_api.department.dto.UpdateDepartmentRequest;
import com.khaled.secure_employee_api.department.entity.Department;
import com.khaled.secure_employee_api.department.exception.DepartmentAlreadyExistsException;
import com.khaled.secure_employee_api.department.exception.DepartmentInUseException;
import com.khaled.secure_employee_api.department.exception.DepartmentNotFoundException;
import com.khaled.secure_employee_api.department.mapper.DepartmentMapper;
import com.khaled.secure_employee_api.department.repository.DepartmentRepository;
import com.khaled.secure_employee_api.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    private final EmployeeRepository employeeRepository;

    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {

        if (departmentRepository.existsByName(request.name())) {
            throw new DepartmentAlreadyExistsException(
                    "Department '" + request.name() + "' already exists."
            );
        }

        Department department =
                departmentMapper.toEntity(request);

        Department savedDepartment =
                departmentRepository.save(department);

        log.info("Department '{}' created successfully.",
                savedDepartment.getName());

        return departmentMapper.toResponse(savedDepartment);
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getDepartment(Long id) {

        Department department = findDepartmentById(id);

        return departmentMapper.toResponse(department);
    }

    @Transactional(readOnly = true)
    public Page<DepartmentResponse> getDepartments(Pageable pageable) {

        return departmentRepository.findAll(pageable)
                .map(departmentMapper::toResponse);
    }

    public DepartmentResponse updateDepartment(
            Long id,
            UpdateDepartmentRequest request
    ) {

        Department department = findDepartmentById(id);

        if (!department.getName().equals(request.name())
                && departmentRepository.existsByName(request.name())) {

            throw new DepartmentAlreadyExistsException(
                    "Department '" + request.name() + "' already exists."
            );
        }

        departmentMapper.updateDepartmentFromRequest(
                request,
                department
        );

        Department updatedDepartment =
                departmentRepository.save(department);

        log.info("Department '{}' updated successfully.",
                updatedDepartment.getName());

        return departmentMapper.toResponse(updatedDepartment);
    }

    public void deleteDepartment(Long id) {

        Department department = findDepartmentById(id);

        validateDepartmentCanBeDeleted(department);

        departmentRepository.delete(department);

        log.info(
                "Department '{}' deleted successfully.",
                department.getName()
        );
    }

    private Department findDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() ->
                        new DepartmentNotFoundException(
                                "Department with id " + id + " not found."
                        )
                );
    }


    private void validateDepartmentCanBeDeleted(Department department) {

        long employeeCount =
                employeeRepository.countByDepartment(department);

        if (employeeCount > 0) {

            throw new DepartmentInUseException(
                    "Department '%s' contains %d employee%s. " +
                            "Move or remove them before deleting the department."
                                    .formatted(
                                            department.getName(),
                                            employeeCount,
                                            employeeCount == 1 ? "" : "s"
                                    )
            );
        }
    }



}