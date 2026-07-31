package com.khaled.secure_employee_api.security.auth.controller;

import com.khaled.secure_employee_api.common.dto.ApiResponse;
import com.khaled.secure_employee_api.security.auth.dto.*;
import com.khaled.secure_employee_api.security.auth.service.AuthenticationService;
import com.khaled.secure_employee_api.common.annotation.RegisterUserApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @RegisterUserApi
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {

        RegisterResponse registerResponse =
                authenticationService.register(registerRequest);

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

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest

    ) {

        TokenResponse tokenResponse =
                authenticationService.login(loginRequest);

        ApiResponse<TokenResponse> response =
                ApiResponse.<TokenResponse>builder()
                        .success(true)
                        .message("User logged in successfully")
                        .data(tokenResponse)
                        .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(
            @Valid @RequestBody RefreshRequest refreshRequest
    ) {

        TokenResponse tokenResponse =
                authenticationService.refreshAuthentication(
                        refreshRequest
                );

        ApiResponse<TokenResponse> response =
                ApiResponse.<TokenResponse>builder()
                        .success(true)
                        .message("Token refreshed successfully")
                        .data(tokenResponse)
                        .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody LogoutRequest request
    ) {

        authenticationService.logout(
                request.refreshToken()
        );

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Logged out successfully")
                        .build()
        );
    }
}
