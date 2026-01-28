package com.init.global.annotation;

import com.init.global.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {PasswordValidator.class})
@Target({FIELD})
@Retention(RUNTIME)
public @interface Password {
    String message() default """
            8~16자의 영문 대소문자, 숫자, 특수문자(@$!%*?&)로 구성해야 하며, 반드시 하나 이상의 영문 소문자와 숫자를 포함해야 합니다."
            """;

    String regexp() default "^(?=.*[a-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,16}$";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
