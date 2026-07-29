package com.khaled.secure_employee_api.service;

import com.khaled.secure_employee_api.dto.RegisterRequest;
import com.khaled.secure_employee_api.dto.RegisterResponse;
import com.khaled.secure_employee_api.exception.UserAlreadyExistsException;
import com.khaled.secure_employee_api.user.entity.AppUser;
import com.khaled.secure_employee_api.user.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {


    private  final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        log.info(
                "User registration attempt for username: {}",
                request.username()
        );


        if (appUserRepository.existsByUsername(request.username())) {
            log.warn(
                    "Registration failed: username already exists: {}",
                    request.username()
            );

            throw new UserAlreadyExistsException(
                    "Username already exists"
            );
        }

        if (appUserRepository.existsByEmail(request.email())) {
            log.warn(
                    "Registration failed: email already exists: {}",
                    request.email()
            );

            throw new UserAlreadyExistsException(
                    "Email already exists"
            );
        }

        String hashedPassword =
                passwordEncoder.encode(request.password());

        AppUser user = AppUser.builder()
                .username(request.username())
                .email(request.email())
                .password(hashedPassword)
                .enabled(true)
                .build();

        AppUser savedUser = appUserRepository.save(user);

        log.info(
                "User registered successfully with username: {}",
                user.getUsername()
        );

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }

}
