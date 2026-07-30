package com.khaled.secure_employee_api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SecureEmployeeApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(SecureEmployeeApiApplication.class, args);
	}

}
