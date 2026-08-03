package com.khaled.secure_employee_api.security.oauth2;

import com.khaled.secure_employee_api.security.auth.dto.TokenResponse;
import com.khaled.secure_employee_api.security.jwt.JwtService;
import com.khaled.secure_employee_api.security.refresh.dto.RefreshTokenRequestContext;
import com.khaled.secure_employee_api.security.refresh.entity.RefreshToken;
import com.khaled.secure_employee_api.security.refresh.service.RefreshTokenService;
import com.khaled.secure_employee_api.user.entity.AppUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationService {

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    private final HttpServletRequest httpServletRequest;

    @Transactional
    public TokenResponse loginWithOAuth2(AppUser appUser) {

        log.info(
                "OAuth2 login successful for '{}'",
                appUser.getEmail()
        );

        String accessToken =
                jwtService.generateAccessToken(appUser);

        RefreshTokenRequestContext context =
                buildRequestContext();

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(
                        appUser,
                        context,
                        true
                );

        return new TokenResponse(
                accessToken,
                refreshToken.getToken(),
                jwtService.getAccessTokenExpiresAt(),
                refreshToken.getExpiresAt()
        );
    }

    private RefreshTokenRequestContext buildRequestContext() {

        String userAgent =
                httpServletRequest.getHeader("User-Agent");

        String ipAddress =
                httpServletRequest.getRemoteAddr();

        String deviceId =
                UUID.randomUUID().toString();

        String deviceName =
                userAgent != null
                        ? userAgent
                        : "Unknown Device";

        return new RefreshTokenRequestContext(
                deviceId,
                deviceName,
                ipAddress,
                userAgent
        );
    }
}