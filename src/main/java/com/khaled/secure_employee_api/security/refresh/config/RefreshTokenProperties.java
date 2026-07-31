package com.khaled.secure_employee_api.security.refresh.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "refresh-token")
public record RefreshTokenProperties(
        long expirationMillis,

        boolean rotationEnabled,

        int maxDevices,

        boolean reuseDetection,

        boolean revokeOnPasswordChange,

        String cleanupCron
) {
}
