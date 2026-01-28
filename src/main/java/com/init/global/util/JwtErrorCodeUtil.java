package com.init.global.util;

import com.init.global.annotation.Util;
import com.init.global.exception.GlobalException;
import com.init.global.exception.payload.BaseErrorCode;
import com.init.infra.security.error.JwtErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

import java.util.Map;
import java.util.Optional;

/**
 * JWT 예외와 오류 코드를 매핑하는 유틸리티 클래스.
 */
@Util
public final class JwtErrorCodeUtil {
    private static final Map<Class<? extends Exception>, JwtErrorCode> ERROR_CODE_MAP = Map.of(
            ExpiredJwtException.class, JwtErrorCode.EXPIRED_TOKEN,
            MalformedJwtException.class, JwtErrorCode.MALFORMED_TOKEN,
            SignatureException.class, JwtErrorCode.TAMPERED_TOKEN,
            UnsupportedJwtException.class, JwtErrorCode.UNSUPPORTED_JWT_TOKEN
    );

    /**
     * 예외에 해당하는 오류 코드를 반환하거나 기본 오류 코드를 반환합니다.
     *
     * @param exception        {@link Exception} : 발생한 예외
     * @param defaultErrorCode {@link JwtErrorCode} : 기본 오류 코드
     * @return {@link GlobalException}
     */
    public static BaseErrorCode determineErrorCode(Exception exception, JwtErrorCode defaultErrorCode) {
        if (exception instanceof GlobalException jwtErrorException)
            return jwtErrorException.getErrorCode();

        Class<? extends Exception> exceptionClass = exception.getClass();
        return ERROR_CODE_MAP.getOrDefault(exceptionClass, defaultErrorCode);
    }

    /**
     * 예외에 해당하는 {@link GlobalException}을 반환합니다.
     * 기본 오류 코드는 400 UNEXPECTED_ERROR 입니다.
     * 해당 메서드는 {@link #determineErrorCode(Exception, JwtErrorCode)} 메서드를 사용합니다.
     *
     * @param exception {@link Exception} : 발생한 예외
     * @return {@link GlobalException}
     */
    public static GlobalException determineAuthErrorException(Exception exception) {
        return findAuthErrorException(exception).orElseGet(
                () -> {
                    BaseErrorCode errorCode = determineErrorCode(exception, JwtErrorCode.UNEXPECTED_ERROR);
                    return new GlobalException(errorCode);
                }
        );
    }

    private static Optional<GlobalException> findAuthErrorException(Exception exception) {
        if (exception instanceof GlobalException) {
            return Optional.of((GlobalException) exception);
        } else if (exception.getCause() instanceof GlobalException) {
            return Optional.of((GlobalException) exception.getCause());
        }
        return Optional.empty();
    }
}
