package com.khaled.secure_employee_api.user.service;

import com.khaled.secure_employee_api.common.exception.RoleNotFoundException;
import com.khaled.secure_employee_api.role.entity.Role;
import com.khaled.secure_employee_api.role.entity.RoleName;
import com.khaled.secure_employee_api.role.repository.RoleRepository;
import com.khaled.secure_employee_api.security.oauth2.GoogleOAuth2UserInfo;
import com.khaled.secure_employee_api.security.user.CustomUserDetails;
import com.khaled.secure_employee_api.user.dto.UserProfileResponse;
import com.khaled.secure_employee_api.user.entity.AppUser;
import com.khaled.secure_employee_api.user.entity.AuthProvider;
import com.khaled.secure_employee_api.user.mapper.UserMapper;
import com.khaled.secure_employee_api.user.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public UserProfileResponse getCurrentUser(Authentication authentication) {

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        return userMapper.toProfileResponse(
                userDetails.getAppUser()
        );
    }

    @Transactional(readOnly = true)
    public AppUser findByEmail(String email) {

        return appUserRepository
                .findByEmail(email)
                .orElse(null);
    }

    public AppUser createOAuthUser(GoogleOAuth2UserInfo userInfo) {

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() ->
                        new RoleNotFoundException(RoleName.USER));

        AppUser user = AppUser.builder()
                .username(generateUsername(userInfo))
                .email(userInfo.getEmail())
                .password("")
                .enabled(true)
                .provider(AuthProvider.GOOGLE)
                .providerId(userInfo.getId())
                .roles(Set.of(userRole))
                .build();

        return appUserRepository.save(user);
    }

    public AppUser updateOAuthUser(
            AppUser user,
            GoogleOAuth2UserInfo userInfo
    ) {

        user.setProvider(AuthProvider.GOOGLE);
        user.setProviderId(userInfo.getId());

        return appUserRepository.save(user);
    }

    private String generateUsername(GoogleOAuth2UserInfo userInfo) {

        String baseUsername =
                userInfo.getEmail().split("@")[0];

        String username = baseUsername;
        int counter = 1;

        while (appUserRepository.existsByUsername(username)) {
            username = baseUsername + counter++;
        }

        return username;
    }
}