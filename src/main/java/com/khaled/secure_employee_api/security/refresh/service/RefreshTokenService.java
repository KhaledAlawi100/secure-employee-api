package com.khaled.secure_employee_api.security.refresh.service;

import com.khaled.secure_employee_api.security.refresh.config.RefreshTokenProperties;
import com.khaled.secure_employee_api.security.refresh.dto.RefreshTokenRequestContext;
import com.khaled.secure_employee_api.security.refresh.entity.RefreshToken;
import com.khaled.secure_employee_api.security.refresh.exception.RefreshTokenNotFoundException;
import com.khaled.secure_employee_api.security.refresh.repository.RefreshTokenRepository;
import com.khaled.secure_employee_api.user.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final RefreshTokenRepository refreshTokenRepository;

    private final RefreshTokenProperties refreshTokenProperties;

    @Transactional
    public RefreshToken createRefreshToken(
            AppUser appUser,
            RefreshTokenRequestContext context,
            boolean enforceDeviceLimit
    ) {

        if (enforceDeviceLimit) {
            enforceDeviceLimit(appUser);
        }

        RefreshToken refreshToken = RefreshToken.builder()
                .token(generateSecureToken())
                .expiresAt(
                        Instant.now()
                                .plusMillis(
                                        refreshTokenProperties.expirationMillis()
                                )
                )
                .deviceId(context.deviceId())
                .deviceName(context.deviceName())
                .ipAddress(context.ipAddress())
                .userAgent(context.userAgent())
                .appUser(appUser)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }
    public RefreshToken findByToken(String token) {

        return refreshTokenRepository.findByToken(token)
                .orElseThrow(RefreshTokenNotFoundException::new);
    }


    public RefreshToken verifyRefreshToken(
            RefreshToken refreshToken
    ) {

        if (refreshToken.isRevoked()) {

            if (refreshTokenProperties.reuseDetection()) {

                revokeAllUserTokens(
                        refreshToken.getAppUser()
                );
            }

            throw new RefreshTokenNotFoundException();
        }

        if (refreshToken.isExpired()) {

            revokeRefreshToken(refreshToken);

            throw new RefreshTokenNotFoundException();
        }

        return refreshToken;
    }


    public void revokeRefreshToken(
            RefreshToken refreshToken
    ) {

        refreshToken.revoke();

        refreshTokenRepository.save(refreshToken);
    }


    @Transactional
    public int revokeAllUserTokens(
            AppUser appUser
    ) {

        return refreshTokenRepository.revokeAllByUser(
                appUser,
                Instant.now()
        );
    }


    public RefreshToken rotateRefreshToken(
            RefreshToken refreshToken
    ) {

        revokeRefreshToken(refreshToken);

        RefreshTokenRequestContext context =
                new RefreshTokenRequestContext(
                        refreshToken.getDeviceId(),
                        refreshToken.getDeviceName(),
                        refreshToken.getIpAddress(),
                        refreshToken.getUserAgent()
                );

        return createRefreshToken(
                refreshToken.getAppUser(),
                context,
                false
        );
    }

    public void enforceDeviceLimit(AppUser appUser) {

        int maxDevices =
                refreshTokenProperties.maxDevices();

        if (maxDevices <= 0) {
            return;
        }

        List<RefreshToken> activeTokens =
                refreshTokenRepository
                        .findByAppUserAndRevokedFalseOrderByCreatedAtAsc(
                                appUser
                        );

        while (activeTokens.size() >= maxDevices) {

            RefreshToken oldestToken =
                    activeTokens.remove(0);

            oldestToken.revoke();

            refreshTokenRepository.save(oldestToken);
        }
    }

    private String generateSecureToken() {

        byte[] randomBytes = new byte[64];

        SECURE_RANDOM.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }
}