package com.khaled.secure_employee_api.user.controller;

import com.khaled.secure_employee_api.common.dto.ApiResponse;
import com.khaled.secure_employee_api.user.dto.UserProfileResponse;
import com.khaled.secure_employee_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getCurrentUser(
            Authentication authentication
    ) {

        UserProfileResponse response =
                userService.getCurrentUser(authentication);

        return ResponseEntity.ok(
                ApiResponse.<UserProfileResponse>builder()
                        .success(true)
                        .message("User profile retrieved successfully.")
                        .data(response)
                        .build()
        );
    }

}