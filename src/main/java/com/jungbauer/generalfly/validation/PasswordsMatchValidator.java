package com.jungbauer.generalfly.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.jungbauer.generalfly.dto.UserDto;

public class PasswordsMatchValidator  implements ConstraintValidator<PasswordsMatch, Object> {
    @Override
    public void initialize(final PasswordsMatch constraintAnnotation) {
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final UserDto user = (UserDto) obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
