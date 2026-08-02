package com.khaled.secure_employee_api.position.controller;

import com.khaled.secure_employee_api.common.dto.ApiResponse;
import com.khaled.secure_employee_api.position.dto.CreatePositionRequest;
import com.khaled.secure_employee_api.position.dto.PositionResponse;
import com.khaled.secure_employee_api.position.dto.UpdatePositionRequest;
import com.khaled.secure_employee_api.position.service.PositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.khaled.secure_employee_api.security.authorization.SecurityExpressions.Position;


@RestController
@RequestMapping("/api/v1/positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @PostMapping
    @PreAuthorize(Position.CREATE)
    public ResponseEntity<ApiResponse<PositionResponse>> createPosition(
            @Valid @RequestBody CreatePositionRequest request
    ) {

        PositionResponse response =
                positionService.createPosition(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<PositionResponse>builder()
                                .success(true)
                                .message("Position created successfully.")
                                .data(response)
                                .build()
                );
    }

    @GetMapping("/{id}")
    @PreAuthorize(Position.READ)
    public ResponseEntity<ApiResponse<PositionResponse>> getPosition(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponse.<PositionResponse>builder()
                        .success(true)
                        .message("Position retrieved successfully.")
                        .data(positionService.getPosition(id))
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize(Position.READ)
    public ResponseEntity<ApiResponse<Page<PositionResponse>>> getPositions(
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                ApiResponse.<Page<PositionResponse>>builder()
                        .success(true)
                        .message("Positions retrieved successfully.")
                        .data(positionService.getPositions(pageable))
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize(Position.UPDATE)
    public ResponseEntity<ApiResponse<PositionResponse>> updatePosition(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePositionRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.<PositionResponse>builder()
                        .success(true)
                        .message("Position updated successfully.")
                        .data(positionService.updatePosition(id, request))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(Position.DELETE)
    public ResponseEntity<ApiResponse<Void>> deletePosition(
            @PathVariable Long id
    ) {

        positionService.deletePosition(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Position deleted successfully.")
                        .build()
        );
    }
}