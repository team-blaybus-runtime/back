package com.init.global.annotation;

import com.init.global.validator.NicknameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {NicknameValidator.class})
@Target({FIELD})
@Retention(RUNTIME)
public @interface Nickname {
    String message() default "한글, 영문, 숫자만을 사용하여 2~10자로 입력되어야 합니다.";

    String regexp() default "^[가-힣a-zA-Z0-9]{2,10}$";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
