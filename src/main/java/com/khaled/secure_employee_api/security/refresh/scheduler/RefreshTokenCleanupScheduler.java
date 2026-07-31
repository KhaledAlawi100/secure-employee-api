package com.khaled.secure_employee_api.security.refresh.scheduler;

import com.khaled.secure_employee_api.security.refresh.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenCleanupScheduler {

    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "${refresh-token.cleanup-cron}")
    @Transactional
    public void cleanupExpiredTokens() {

        Instant now = Instant.now();

        refreshTokenRepository.deleteByExpiresAtBefore(now);

        log.info(
                "Refresh token cleanup completed at {}",
                now
        );
    }
}