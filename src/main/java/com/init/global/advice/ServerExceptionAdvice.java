package com.init.global.advice;

import com.init.domain.business.user.error.UserErrorCode;
import com.init.global.exception.payload.CausedBy;
import com.init.global.exception.payload.ErrorResponse;
import com.init.global.exception.payload.ReasonCode;
import com.init.global.exception.payload.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class ServerExceptionAdvice {
    /**
     * API 호출 시 인가 관련 예외를 처리하는 메서드
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    protected ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        log.warn("handleAccessDeniedException : {}", e.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.FORBIDDEN, ReasonCode.ACCESS_TO_THE_REQUESTED_RESOURCE_IS_FORBIDDEN);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * 잘못된 URL 호출 시
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ErrorResponse handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.warn("handleNoHandlerFoundException : {}", e.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.NOT_FOUND, ReasonCode.INVALID_URL_OR_ENDPOINT);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * 존재하지 않는 URL 호출 시
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    protected ErrorResponse handleNoResourceFoundException(NoResourceFoundException e) {
        log.warn("handleNoResourceFoundException : {}", e.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.NOT_FOUND, ReasonCode.INVALID_URL_OR_ENDPOINT);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * API 호출 시 데이터를 반환할 수 없는 경우
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpMessageNotWritableException.class)
    protected ErrorResponse handleHttpMessageNotWritableException(HttpMessageNotWritableException e) {
        log.warn("handleHttpMessageNotWritableException : {}", e.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.INTERNAL_SERVER_ERROR, ReasonCode.UNEXPECTED_ERROR);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * NullPointerException이 발생한 경우
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    protected ErrorResponse handleNullPointerException(NullPointerException e) {
        log.warn("handleNullPointerException : {}", e.getMessage());
        e.printStackTrace();
        CausedBy causedBy = CausedBy.of(StatusCode.INTERNAL_SERVER_ERROR, ReasonCode.UNEXPECTED_ERROR);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * @PreAuthorize 에서 검증 실패 시 발생하는 AuthorizationDeniedException 처리
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationDeniedException.class)
    protected ErrorResponse handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        log.warn("handleAuthorizationDeniedException : {}", e.getMessage());
        UserErrorCode errorCode = UserErrorCode.FORBIDDEN;
        CausedBy causedBy = errorCode.causedBy();
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), errorCode.getExplainError());
    }

    /**
     * 기타 예외가 발생한 경우
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ErrorResponse handleException(Exception e) {
        log.warn("{} : handleException : {}", e.getClass(), e.getMessage());
        e.printStackTrace();
        CausedBy causedBy = CausedBy.of(StatusCode.INTERNAL_SERVER_ERROR, ReasonCode.UNEXPECTED_ERROR);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }
}
