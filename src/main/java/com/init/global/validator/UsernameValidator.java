package com.init.global.validator;

import com.init.global.annotation.Username;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<Username, String> {
    private String regexp;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && Pattern.matches(regexp, value);
    }

    @Override
    public void initialize(Username constraintAnnotation) {
        this.regexp = constraintAnnotation.regexp();
    }
}
