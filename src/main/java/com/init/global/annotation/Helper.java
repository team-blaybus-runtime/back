package com.init.global.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 컴포넌트 등록이 필요한 유틸리티성 클래스를 명시하는 어노테이션입니다.
 * <p>
 * 이 어노테이션은 Spring 빈으로 등록되어야 하며, 상태를 가지지 않는 <b>Helper</b> 역할의 클래스를 나타냅니다.<br>
 * 컴포넌트 등록이 필요 없는 static 메소드만 제공하는 경우에는 {@link Util} 어노테이션을 사용하세요.
 * </p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Helper {
}
