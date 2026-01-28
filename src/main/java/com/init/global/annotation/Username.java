package com.init.global.annotation;

import com.init.global.validator.UsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {UsernameValidator.class})
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Username {
    String message() default "영문 소문자, 숫자만을 사용하여 5~15자로 입력되어야 합니다.";

    String regexp() default "^[a-z0-9-_.]{5,15}$";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
