
package com.khaled.secure_employee_api.dto;

public record LoginResponse(

        String accessToken,
        long expiresIn

) {

}

