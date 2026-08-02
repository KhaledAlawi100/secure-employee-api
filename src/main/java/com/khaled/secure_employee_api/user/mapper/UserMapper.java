package com.khaled.secure_employee_api.user.mapper;

import com.khaled.secure_employee_api.user.dto.UserProfileResponse;
import com.khaled.secure_employee_api.user.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(
            target = "roles",
            expression = "java(user.getRoles().stream()"
                    + ".map(role -> role.getName().name())"
                    + ".collect(java.util.stream.Collectors.toSet()))"
    )
    UserProfileResponse toProfileResponse(AppUser user);

}