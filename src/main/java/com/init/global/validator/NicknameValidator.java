package com.init.global.validator;

import com.init.global.annotation.Nickname;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class NicknameValidator implements ConstraintValidator<Nickname, String> {
    private String regexp;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && Pattern.matches(regexp, value);
    }

    @Override
    public void initialize(Nickname constraintAnnotation) {
        this.regexp = constraintAnnotation.regexp();
    }
}
