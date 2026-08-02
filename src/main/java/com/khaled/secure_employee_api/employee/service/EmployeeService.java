package com.khaled.secure_employee_api.employee.service;

import com.khaled.secure_employee_api.common.dto.PageResponse;
import com.khaled.secure_employee_api.common.exception.UserNotFoundException;
import com.khaled.secure_employee_api.common.mapper.PageMapper;
import com.khaled.secure_employee_api.department.entity.Department;
import com.khaled.secure_employee_api.department.exception.DepartmentNotFoundException;
import com.khaled.secure_employee_api.department.repository.DepartmentRepository;
import com.khaled.secure_employee_api.employee.dto.*;
import com.khaled.secure_employee_api.employee.exception.EmployeeAlreadyExistsException;
import com.khaled.secure_employee_api.employee.exception.EmployeeNotFoundException;
import com.khaled.secure_employee_api.employee.mapper.EmployeeMapper;
import com.khaled.secure_employee_api.employee.model.Employee;
import com.khaled.secure_employee_api.employee.model.EmployeeStatus;
import com.khaled.secure_employee_api.employee.repository.EmployeeRepository;
import com.khaled.secure_employee_api.position.entity.Position;
import com.khaled.secure_employee_api.position.exception.PositionNotFoundException;
import com.khaled.secure_employee_api.position.repository.PositionRepository;
import com.khaled.secure_employee_api.user.entity.AppUser;
import com.khaled.secure_employee_api.user.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.khaled.secure_employee_api.employee.dto.UpdateEmployeeStatusRequest;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AppUserRepository appUserRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeMapper employeeMapper;
    private final PageMapper pageMapper;

    public EmployeeAdminResponse createEmployee(CreateEmployeeRequest request) {

        AppUser user = findUser(request.userId());

        validateUserNotAssigned(user.getId());

        Department department = findDepartment(request.departmentId());

        Position position = findPosition(request.positionId());

        Employee manager = request.managerId() == null
                ? null
                : findEmployee(request.managerId());

        Employee employee = employeeMapper.toEntity(request);

        employee.setUser(user);
        employee.setDepartment(department);
        employee.setPosition(position);
        employee.setManager(manager);

        employee.setApproved(false);
        employee.setStatus(EmployeeStatus.PENDING);

        Employee savedEmployee = employeeRepository.save(employee);

        log.info(
                "Employee '{}' created successfully for user '{}'.",
                savedEmployee.getId(),
                user.getUsername()
        );

        return employeeMapper.toAdminResponse(savedEmployee);
    }

    @Transactional(readOnly = true)
    public EmployeeAdminResponse getEmployeeById(Long employeeId) {

        return employeeMapper.toAdminResponse(findEmployee(employeeId));
    }

    @Transactional(readOnly = true)
    public PageResponse<EmployeeAdminResponse> getEmployees(Pageable pageable) {

        Page<EmployeeAdminResponse> page = employeeRepository
                .findAll(pageable)
                .map(employeeMapper::toAdminResponse);

        return pageMapper.toPageResponse(page);
    }

    @Transactional(readOnly = true)
    public EmployeeAdminResponse getEmployeeByUsername(String username) {

        Employee employee = employeeRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found for username: " + username
                        ));

        return employeeMapper.toAdminResponse(employee);
    }

    @Transactional(readOnly = true)
    public EmployeeProfileResponse getCurrentEmployee(String username) {

        Employee employee = employeeRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found for username: " + username
                        ));

        return employeeMapper.toProfileResponse(employee);
    }


    public void deleteEmployee(Long employeeId) {

        Employee employee = findEmployee(employeeId);

        employeeRepository.delete(employee);

        log.info(
                "Employee '{}' deleted successfully.",
                employeeId
        );
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getPublicEmployee(Long employeeId) {

        return employeeMapper.toResponse(findEmployee(employeeId));
    }

    @Transactional(readOnly = true)
    public EmployeeSummaryResponse getEmployeeSummary(Long employeeId) {

        return employeeMapper.toSummaryResponse(findEmployee(employeeId));
    }

    @Transactional(readOnly = true)
    public PageResponse<EmployeeSummaryResponse> getEmployeeSummaries(Pageable pageable) {

        Page<EmployeeSummaryResponse> page =
                employeeRepository.findAll(pageable)
                        .map(employeeMapper::toSummaryResponse);

        return pageMapper.toPageResponse(page);
    }

    public EmployeeAdminResponse updateEmployee(
            Long employeeId,
            UpdateEmployeeRequest request
    ) {

        Employee employee = findEmployee(employeeId);

        employeeMapper.updateEmployeeFromRequest(request, employee);

        employee.setDepartment(
                findDepartment(request.departmentId())
        );

        employee.setPosition(
                findPosition(request.positionId())
        );

        employee.setManager(
                request.managerId() == null
                        ? null
                        : findEmployee(request.managerId())
        );

        Employee updatedEmployee = employeeRepository.save(employee);

        log.info(
                "Employee '{}' updated successfully.",
                updatedEmployee.getId()
        );

        return employeeMapper.toAdminResponse(updatedEmployee);
    }

    public EmployeeAdminResponse updateEmployeeStatus(
            Long employeeId,
            UpdateEmployeeStatusRequest request
    ) {

        Employee employee = findEmployee(employeeId);

        validateStatusTransition(
                employee.getStatus(),
                request.status()
        );

        EmployeeStatus oldStatus = employee.getStatus();

        employee.setStatus(request.status());

        employee.setApproved(
                request.status() == EmployeeStatus.ACTIVE
        );

        Employee updatedEmployee = employeeRepository.save(employee);

        log.info(
                "Employee '{}' status changed from '{}' to '{}'.",
                updatedEmployee.getId(),
                oldStatus,
                updatedEmployee.getStatus()
        );

        return employeeMapper.toAdminResponse(updatedEmployee);
    }

    private void validateStatusTransition(
            EmployeeStatus currentStatus,
            EmployeeStatus newStatus
    ) {

        switch (currentStatus) {

            case PENDING -> {

                if (newStatus != EmployeeStatus.ACTIVE &&
                        newStatus != EmployeeStatus.REJECTED) {

                    throw new IllegalStateException(
                            "A pending employee can only become ACTIVE or REJECTED."
                    );
                }
            }

            case ACTIVE -> {

                if (newStatus != EmployeeStatus.SUSPENDED &&
                        newStatus != EmployeeStatus.TERMINATED) {

                    throw new IllegalStateException(
                            "An active employee can only become SUSPENDED or TERMINATED."
                    );
                }
            }

            case SUSPENDED -> {

                if (newStatus != EmployeeStatus.ACTIVE &&
                        newStatus != EmployeeStatus.TERMINATED) {

                    throw new IllegalStateException(
                            "A suspended employee can only become ACTIVE or TERMINATED."
                    );
                }
            }

            case REJECTED, TERMINATED ->

                    throw new IllegalStateException(
                            "This employee status cannot be changed."
                    );
        }
    }




    private Employee findEmployee(Long employeeId) {

        return employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with id: " + employeeId
                        ));
    }

    private AppUser findUser(Long userId) {

        return appUserRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + userId
                        ));
    }

    private Department findDepartment(Long departmentId) {

        return departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new DepartmentNotFoundException(
                                "Department not found with id: " + departmentId
                        ));
    }

    private Position findPosition(Long positionId) {

        return positionRepository.findById(positionId)
                .orElseThrow(() ->
                        new PositionNotFoundException(
                                "Position not found with id: " + positionId
                        ));
    }

    private void validateUserNotAssigned(Long userId) {

        if (employeeRepository.existsByUserId(userId)) {
            throw new EmployeeAlreadyExistsException(
                    "This user already has an employee profile."
            );
        }
    }



}