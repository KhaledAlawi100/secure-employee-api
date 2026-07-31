package com.khaled.secure_employee_api.security.refresh.repository;

import com.khaled.secure_employee_api.security.refresh.entity.RefreshToken;
import com.khaled.secure_employee_api.user.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

//    List<RefreshToken> findAllByAppUser(AppUser appUser);

//    void deleteByAppUser(AppUser appUser);

    void deleteByExpiresAtBefore(Instant instant);

    @Modifying
    @Query("""
            UPDATE RefreshToken r
            SET r.revoked = true,
                r.revokedAt = :revokedAt
            WHERE r.appUser = :appUser
              AND r.revoked = false
            """)
    int revokeAllByUser(
            @Param("appUser") AppUser appUser,
            @Param("revokedAt") Instant revokedAt
    );

    List<RefreshToken> findByAppUserAndRevokedFalseOrderByCreatedAtAsc(
            AppUser appUser
    );


}
