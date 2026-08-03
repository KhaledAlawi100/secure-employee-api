package com.khaled.secure_employee_api.security.oauth2;

import com.khaled.secure_employee_api.common.exception.RoleNotFoundException;
import com.khaled.secure_employee_api.role.entity.Role;
import com.khaled.secure_employee_api.role.entity.RoleName;
import com.khaled.secure_employee_api.role.repository.RoleRepository;
import com.khaled.secure_employee_api.user.entity.AppUser;
import com.khaled.secure_employee_api.user.entity.AuthProvider;
import com.khaled.secure_employee_api.user.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AppUserRepository appUserRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public OAuth2User loadUser(
            OAuth2UserRequest userRequest
    ) throws OAuth2AuthenticationException {

        System.out.println("CUSTOM OAUTH2 SERVICE CALLED");

        OAuth2User oauth2User =
                super.loadUser(userRequest);

        GoogleOAuth2UserInfo userInfo =
                new GoogleOAuth2UserInfo(
                        oauth2User.getAttributes()
                );

        log.info(
                "OAuth2 login received from provider: {}",
                userRequest
                        .getClientRegistration()
                        .getRegistrationId()
        );

        appUserRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> createOAuth2User(userInfo));

        return oauth2User;
    }

    private AppUser createOAuth2User(
            GoogleOAuth2UserInfo userInfo
    ) {

        log.info(
                "Creating new OAuth2 user '{}'",
                userInfo.getEmail()
        );

        Role userRole =
                roleRepository.findByName(RoleName.USER)
                        .orElseThrow(() ->
                                new RoleNotFoundException(
                                        RoleName.USER
                                )
                        );

        AppUser appUser =
                AppUser.builder()
                        .username(generateUsername(userInfo))
                        .email(userInfo.getEmail())
                        .password(
                                passwordEncoder.encode(
                                        UUID.randomUUID().toString()
                                )
                        )
                        .enabled(true)
                        .provider(AuthProvider.GOOGLE)
                        .providerId(userInfo.getId())
                        .roles(Set.of(userRole))
                        .build();

        log.info("Saving OAuth2 user...");



        AppUser saved = appUserRepository.save(appUser);

        log.info(
                "OAuth2 user saved successfully with id={}",
                saved.getId()
        );

        return saved;    }

    private String generateUsername(
            GoogleOAuth2UserInfo userInfo
    ) {

        String baseUsername =
                userInfo.getEmail()
                        .split("@")[0];

        String username =
                baseUsername;

        int counter = 1;

        while (appUserRepository.existsByUsername(username)) {

            username =
                    baseUsername + counter;

            counter++;
        }

        return username;
    }
}