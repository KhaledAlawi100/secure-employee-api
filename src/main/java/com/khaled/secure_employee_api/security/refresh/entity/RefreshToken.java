package com.khaled.secure_employee_api.security.refresh.entity;

import com.khaled.secure_employee_api.user.entity.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "refresh_tokens",
        indexes = {
                @Index(
                        name = "idx_refresh_token_user_id",
                        columnList = "user_id"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 512
    )
    private String token;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(
            nullable = false,
            updatable = false
    )
    private Instant createdAt;

    @Column(nullable = false)
    @Builder.Default
    private boolean revoked = false;

    private Instant revokedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private AppUser appUser;

    @Column(length = 255)
    private String deviceId;

    @Column(length = 255)
    private String deviceName;

    @Column(length = 45)
    private String ipAddress;

    @Column(length = 1000)
    private String userAgent;



    @PrePersist
    public void setCreationTimestamp() {
        createdAt = Instant.now();
    }

    public boolean isExpired() {
        return expiresAt.isBefore(Instant.now());
    }

    public void revoke() {
        this.revoked = true;
        this.revokedAt = Instant.now();
    }
}