package com.khaled.secure_employee_api.security.controller;

import com.khaled.secure_employee_api.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/security")
@RequiredArgsConstructor
public class SecurityContextController {

    @GetMapping("/authorities")
    public ResponseEntity<ApiResponse<List<String>>> getAuthorities(
            Authentication authentication
    ) {

        List<String> authorities =
                authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

        return ResponseEntity.ok(
                ApiResponse.<List<String>>builder()
                        .success(true)
                        .message("Authorities loaded successfully")
                        .data(authorities)
                        .build()
        );
    }
}