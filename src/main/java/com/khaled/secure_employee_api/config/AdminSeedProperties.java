package com.khaled.secure_employee_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "admin")
public class AdminSeedProperties {

    private String username;

    private String email;

    private String password;
}