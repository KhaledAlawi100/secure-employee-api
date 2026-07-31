
package com.khaled.secure_employee_api.security.auth.dto;

import java.time.Instant;

public record TokenResponse(

        String accessToken,
        String refreshToken,
        Instant accessTokenExpiresAt,
        Instant refreshTokenExpiresAt

) {

}

