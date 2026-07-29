package com.khaled.secure_employee_api.validation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator
        implements ConstraintValidator<StrongPassword, String> {

    private static final int MIN_PASSWORD_LENGTH = 8;

    @Override
    public boolean isValid(
            String password,
            ConstraintValidatorContext context
    ) {

        if (password == null || password.isBlank()) {
            return false;
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }

        boolean hasUppercase = password.matches(".*[A-Z].*");

        boolean hasLowercase = password.matches(".*[a-z].*");

        boolean hasDigit = password.matches(".*\\d.*");

        boolean hasSpecialCharacter =
                password.matches(".*[@$!%*?&].*");

        return hasUppercase
                && hasLowercase
                && hasDigit
                && hasSpecialCharacter;
    }
}


