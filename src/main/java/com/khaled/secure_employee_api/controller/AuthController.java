package com.khaled.secure_employee_api.controller;

import com.khaled.secure_employee_api.annotation.RegisterUserApi;
import com.khaled.secure_employee_api.dto.ApiResponse;
import com.khaled.secure_employee_api.dto.RegisterRequest;
import com.khaled.secure_employee_api.dto.RegisterResponse;
import com.khaled.secure_employee_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @RegisterUserApi
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {

        RegisterResponse registerResponse = authService.register(registerRequest);

        ApiResponse<RegisterResponse> response =
                ApiResponse.<RegisterResponse>builder()
                .success(true)
                .message("User registered successfully")
                .data(registerResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }
}
