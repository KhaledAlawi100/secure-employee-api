package com.khaled.secure_employee_api.security.auth.service;

import com.khaled.secure_employee_api.common.exception.RoleNotFoundException;
import com.khaled.secure_employee_api.common.exception.UserAlreadyExistsException;
import com.khaled.secure_employee_api.role.entity.Role;
import com.khaled.secure_employee_api.role.entity.RoleName;
import com.khaled.secure_employee_api.role.repository.RoleRepository;
import com.khaled.secure_employee_api.security.auth.dto.LoginRequest;
import com.khaled.secure_employee_api.security.auth.dto.RefreshRequest;
import com.khaled.secure_employee_api.security.auth.dto.RegisterRequest;
import com.khaled.secure_employee_api.security.auth.dto.RegisterResponse;
import com.khaled.secure_employee_api.security.auth.dto.TokenResponse;
import com.khaled.secure_employee_api.security.jwt.JwtService;
import com.khaled.secure_employee_api.security.refresh.dto.RefreshTokenRequestContext;
import com.khaled.secure_employee_api.security.refresh.entity.RefreshToken;
import com.khaled.secure_employee_api.security.refresh.service.RefreshTokenService;
import com.khaled.secure_employee_api.security.user.CustomUserDetails;
import com.khaled.secure_employee_api.user.entity.AppUser;
import com.khaled.secure_employee_api.user.repository.AppUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    private final HttpServletRequest httpServletRequest;

    private final RoleRepository roleRepository;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        log.info(
                "User registration attempt for username: {}",
                request.username()
        );

        validateRegistrationRequest(request);

        Role userRole = getDefaultUserRole();

        AppUser appUser = buildUser(request, userRole);

        AppUser savedUser = appUserRepository.save(appUser);

        log.info(
                "User '{}' registered successfully with role '{}'",
                savedUser.getUsername(),
                userRole.getName()
        );

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }

    public TokenResponse login(LoginRequest request) {

        log.info(
                "Login attempt for username: {}",
                request.username()
        );

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password()
                        )
                );

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String accessToken =
                jwtService.generateToken(userDetails);

        RefreshTokenRequestContext context =
                buildRequestContext();

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(
                        userDetails.getAppUser(),
                        context,
                        true
                );

        log.info(
                "User logged in successfully: {}",
                request.username()
        );

        return new TokenResponse(
                accessToken,
                refreshToken.getToken(),
                jwtService.getAccessTokenExpiresAt(),
                refreshToken.getExpiresAt()
        );
    }



    @Transactional
    public TokenResponse refreshAuthentication(
            RefreshRequest request
    ) {

        log.info("Refresh token authentication attempt");

        RefreshToken refreshToken =
                refreshTokenService.findByToken(
                        request.refreshToken()
                );

        refreshTokenService.verifyRefreshToken(
                refreshToken
        );

        RefreshToken rotatedRefreshToken =
                refreshTokenService.rotateRefreshToken(
                        refreshToken
                );

        AppUser appUser =
                rotatedRefreshToken.getAppUser();

        String accessToken =
                jwtService.generateAccessToken(appUser);

        log.info(
                "Refresh token authentication successful for username: {}",
                appUser.getUsername()
        );

        return new TokenResponse(
                accessToken,
                rotatedRefreshToken.getToken(),
                jwtService.getAccessTokenExpiresAt(),
                rotatedRefreshToken.getExpiresAt()
        );
    }

    public void logout(String refreshTokenValue) {

        RefreshToken refreshToken =
                refreshTokenService.findByToken(refreshTokenValue);

        refreshTokenService.revokeRefreshToken(refreshToken);
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

    private void validateRegistrationRequest(RegisterRequest request) {

        if (appUserRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException(
                    "Username already exists"
            );
        }

        if (appUserRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(
                    "Email already exists"
            );
        }
    }

    private Role getDefaultUserRole() {

        return roleRepository.findByName(RoleName.USER)
                .orElseThrow(() ->
                        new RoleNotFoundException(RoleName.USER)
                );
    }

    private AppUser buildUser(
            RegisterRequest request,
            Role userRole
    ) {

        return AppUser.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .enabled(true)
                .roles(Set.of(userRole))
                .build();
    }
}