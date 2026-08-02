package com.khaled.secure_employee_api.department.controller;

import com.khaled.secure_employee_api.common.dto.ApiResponse;
import com.khaled.secure_employee_api.department.dto.CreateDepartmentRequest;
import com.khaled.secure_employee_api.department.dto.DepartmentResponse;
import com.khaled.secure_employee_api.department.dto.UpdateDepartmentRequest;
import com.khaled.secure_employee_api.department.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.khaled.secure_employee_api.security.authorization.SecurityExpressions.Department;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    @PreAuthorize(Department.CREATE)
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(
            @Valid @RequestBody CreateDepartmentRequest request
    ) {

        DepartmentResponse response =
                departmentService.createDepartment(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<DepartmentResponse>builder()
                                .success(true)
                                .message("Department created successfully.")
                                .data(response)
                                .build()
                );
    }

    @GetMapping("/{id}")
    @PreAuthorize(Department.READ)
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartment(
            @PathVariable Long id
    ) {

        DepartmentResponse response =
                departmentService.getDepartment(id);

        return ResponseEntity.ok(
                ApiResponse.<DepartmentResponse>builder()
                        .success(true)
                        .message("Department retrieved successfully.")
                        .data(response)
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize(Department.READ)
    public ResponseEntity<ApiResponse<Page<DepartmentResponse>>> getDepartments(
            Pageable pageable
    ) {

        Page<DepartmentResponse> response =
                departmentService.getDepartments(pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<DepartmentResponse>>builder()
                        .success(true)
                        .message("Departments retrieved successfully.")
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize(Department.UPDATE)
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDepartmentRequest request
    ) {

        DepartmentResponse response =
                departmentService.updateDepartment(id, request);

        return ResponseEntity.ok(
                ApiResponse.<DepartmentResponse>builder()
                        .success(true)
                        .message("Department updated successfully.")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(Department.DELETE)
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(
            @PathVariable Long id
    ) {

        departmentService.deleteDepartment(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Department deleted successfully.")
                        .build()
        );
    }
}