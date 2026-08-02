package com.khaled.secure_employee_api.security.current;

import com.khaled.secure_employee_api.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    public CustomUserDetails getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return (CustomUserDetails) authentication.getPrincipal();
    }

    public Long getUserId() {
        return getCurrentUser().getId();
    }

    public String getUsername() {
        return getCurrentUser().getUsername();
    }
}