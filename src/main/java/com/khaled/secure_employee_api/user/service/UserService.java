package com.khaled.secure_employee_api.user.service;

import com.khaled.secure_employee_api.security.user.CustomUserDetails;
import com.khaled.secure_employee_api.user.dto.UserProfileResponse;
import com.khaled.secure_employee_api.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public UserProfileResponse getCurrentUser(
            Authentication authentication
    ) {

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        return userMapper.toProfileResponse(
                userDetails.getAppUser()
        );
    }
}