package com.khaled.secure_employee_api.security.oauth2;

import com.khaled.secure_employee_api.security.auth.dto.TokenResponse;
import com.khaled.secure_employee_api.user.entity.AppUser;
import com.khaled.secure_employee_api.user.repository.AppUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AppUserRepository appUserRepository;

    private final OAuth2AuthenticationService oauth2AuthenticationService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oauth2User =
                (OAuth2User) authentication.getPrincipal();

        GoogleOAuth2UserInfo userInfo =
                new GoogleOAuth2UserInfo(oauth2User.getAttributes());

        log.info("OAuth2 login successful.");
        log.info("Provider Id: {}", userInfo.getId());
        log.info("Email: {}", userInfo.getEmail());
        log.info("Full Name: {}", userInfo.getFullName());

        AppUser appUser =
                appUserRepository.findByEmail(userInfo.getEmail())
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "OAuth2 user was not found after authentication."
                                )
                        );

        TokenResponse tokenResponse =
                oauth2AuthenticationService.loginWithOAuth2(appUser);

        String redirectUrl =
                "http://localhost:3000/oauth2/success"
                        + "?accessToken=" + tokenResponse.accessToken()
                        + "&refreshToken=" + tokenResponse.refreshToken();

        response.sendRedirect(redirectUrl);
    }
}