package com.khaled.secure_employee_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class OpenApiConfig {

    @Bean
    public OpenAPI secureEmployeeOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Secure Employee API")
                                .version("v1.0")
                                .description(
                                        "REST API for the Secure Employee Management System"
                                )
                );
    }


}