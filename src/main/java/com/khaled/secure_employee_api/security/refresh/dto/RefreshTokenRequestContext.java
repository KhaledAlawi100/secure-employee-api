package com.khaled.secure_employee_api.security.refresh.dto;

public record RefreshTokenRequestContext(

        String deviceId,

        String deviceName,

        String ipAddress,

        String userAgent

) {
}