package com.khaled.secure_employee_api.employee.controller;

import com.khaled.secure_employee_api.common.dto.ApiResponse;
import com.khaled.secure_employee_api.common.dto.PageResponse;
import com.khaled.secure_employee_api.employee.dto.*;
import com.khaled.secure_employee_api.employee.service.EmployeeService;
import com.khaled.secure_employee_api.security.current.CurrentUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.khaled.secure_employee_api.security.authorization.SecurityExpressions.Employee.CREATE;
import static com.khaled.secure_employee_api.security.authorization.SecurityExpressions.Employee.DELETE;
import static com.khaled.secure_employee_api.security.authorization.SecurityExpressions.Employee.UPDATE;
import static com.khaled.secure_employee_api.security.authorization.SecurityExpressions.Roles.ADMIN;
import static com.khaled.secure_employee_api.security.authorization.SecurityExpressions.Roles.USER;
import static com.khaled.secure_employee_api.security.authorization.SecurityExpressions.Common.AUTHENTICATED;


@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;
    private final CurrentUserService currentUserService;

    @PostMapping
    @PreAuthorize(CREATE)
    public ResponseEntity<ApiResponse<EmployeeAdminResponse>> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request
    ) {

        EmployeeAdminResponse employee =
                employeeService.createEmployee(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<EmployeeAdminResponse>builder()
                                .success(true)
                                .message("Employee created successfully")
                                .data(employee)
                                .build()
                );
    }

    @GetMapping("/{id}")
    @PreAuthorize(ADMIN)
    public ResponseEntity<ApiResponse<EmployeeAdminResponse>> getEmployeeById(

            @PathVariable
            @Positive(message = "Employee id must be positive")
            Long id
    ) {

        EmployeeAdminResponse employee =
                employeeService.getEmployeeById(id);

        return ResponseEntity.ok(
                ApiResponse.<EmployeeAdminResponse>builder()
                        .success(true)
                        .message("Employee retrieved successfully")
                        .data(employee)
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize(ADMIN)
    public ResponseEntity<ApiResponse<PageResponse<EmployeeAdminResponse>>> getEmployees(

            @RequestParam(defaultValue = "0")
            @Min(value = 0)
            int page,

            @RequestParam(defaultValue = "10")
            @Min(1)
            @Max(100)
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "ASC")
            Sort.Direction direction
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sortBy)
        );

        PageResponse<EmployeeAdminResponse> employees =
                employeeService.getEmployees(pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<EmployeeAdminResponse>>builder()
                        .success(true)
                        .message("Employees retrieved successfully")
                        .data(employees)
                        .build()
        );
    }

    @GetMapping("/username/{username}")
    @PreAuthorize(ADMIN)
    public ResponseEntity<ApiResponse<EmployeeAdminResponse>> getEmployeeByUsername(

            @PathVariable
            @NotBlank(message = "Username is required")
            String username
    ) {

        EmployeeAdminResponse employee =
                employeeService.getEmployeeByUsername(username);

        return ResponseEntity.ok(
                ApiResponse.<EmployeeAdminResponse>builder()
                        .success(true)
                        .message("Employee retrieved successfully")
                        .data(employee)
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize(UPDATE)
    public ResponseEntity<ApiResponse<EmployeeAdminResponse>> updateEmployee(

            @PathVariable
            Long id,

            @Valid
            @RequestBody UpdateEmployeeRequest request
    ) {

        EmployeeAdminResponse employee =
                employeeService.updateEmployee(id, request);

        return ResponseEntity.ok(
                ApiResponse.<EmployeeAdminResponse>builder()
                        .success(true)
                        .message("Employee updated successfully")
                        .data(employee)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(DELETE)
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(
            @PathVariable Long id
    ) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Employee deleted successfully")
                        .build()
        );
    }

    @GetMapping("/me")
    @PreAuthorize(AUTHENTICATED)
    public ResponseEntity<ApiResponse<EmployeeProfileResponse>> getCurrentEmployee() {

        EmployeeProfileResponse employee =
                employeeService.getCurrentEmployee(
                        currentUserService.getUsername()
                );

        return ResponseEntity.ok(
                ApiResponse.<EmployeeProfileResponse>builder()
                        .success(true)
                        .message("Current employee retrieved successfully")
                        .data(employee)
                        .build()
        );
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize(UPDATE)
    public ResponseEntity<ApiResponse<EmployeeAdminResponse>> updateEmployeeStatus(

            @PathVariable
            @Positive(message = "Employee id must be positive")
            Long id,

            @Valid
            @RequestBody UpdateEmployeeStatusRequest request
    ) {

        EmployeeAdminResponse employee =
                employeeService.updateEmployeeStatus(id, request);

        return ResponseEntity.ok(
                ApiResponse.<EmployeeAdminResponse>builder()
                        .success(true)
                        .message("Employee status updated successfully")
                        .data(employee)
                        .build()
        );
    }
}