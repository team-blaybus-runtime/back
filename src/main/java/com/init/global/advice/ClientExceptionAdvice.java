package com.init.global.advice;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.init.global.exception.payload.CausedBy;
import com.init.global.exception.payload.ErrorResponse;
import com.init.global.exception.payload.ReasonCode;
import com.init.global.exception.payload.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ClientExceptionAdvice {

    /**
     * API 호출 시 'Cookie' 내에 데이터 값이 유효하지 않은 경우
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestCookieException.class)
    protected ErrorResponse handleMissingRequestCookieException(MissingRequestCookieException e) {
        log.warn("handleMissingRequestCookieException : {}", e.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.BAD_REQUEST, ReasonCode.MISSING_REQUIRED_PARAMETER);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * API 호출 시 'Method' 내에 데이터 값이 유효하지 않은 경우
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("handleHttpRequestMethodNotSupportedException : {}", e.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.BAD_REQUEST, ReasonCode.INVALID_REQUEST);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * API 호출 시 'Header' 내에 데이터 값이 유효하지 않은 경우
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ErrorResponse handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        log.warn("handleMissingRequestHeaderException : {}", e.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.BAD_REQUEST, ReasonCode.MISSING_REQUIRED_PARAMETER);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * API 호출 시 'Parameter' 내에 데이터 값이 존재하지 않은 경우
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ErrorResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.warn("handleMissingServletRequestParameterException : {}", e.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.BAD_REQUEST, ReasonCode.MISSING_REQUIRED_PARAMETER);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }


    /**
     * Validation 어노테이션(@Valid, @Validated) 적용 시 데이터 값이 유효하지 않은 경우
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ErrorResponse handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        log.warn("handleHandlerMethodValidationException : {}", e.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.BAD_REQUEST, ReasonCode.MISSING_REQUIRED_PARAMETER);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * API 호출 시 객체 혹은 파라미터 데이터 값이 유효하지 않은 경우
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("handleMethodArgumentNotValidException: {}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        return ErrorResponse.failure(bindingResult, ReasonCode.REQUIRED_PARAMETERS_MISSING_IN_REQUEST_BODY);
    }

    /**
     * API 호출 시 객체 혹은 파라미터 데이터 값이 유효하지 않은 경우
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("handleMethodArgumentTypeMismatchException: {}", e.getMessage());
        Class<?> type = e.getRequiredType();
        assert type != null;
        Map<String, String> fieldErrors = new HashMap<>();
        if (type.isEnum()) {
            String message = "The parameter " + e.getName() + " must have a value among : " + StringUtils.join(type.getEnumConstants(), ", ");
            fieldErrors.put(e.getName(), message);
        } else {
            fieldErrors.put(e.getName(), "The parameter " + e.getName() + " must have a value of type " + type.getSimpleName());
        }
        CausedBy causedBy = CausedBy.of(StatusCode.UNPROCESSABLE_CONTENT, ReasonCode.TYPE_MISMATCH_ERROR_IN_REQUEST_BODY);
        return ErrorResponse.failure(causedBy.getCode(), causedBy.getReason(), e.getMessage(), fieldErrors);
    }

    /**
     * JSON 형식의 요청 데이터를 파싱하는 과정에서 발생하는 예외를 처리하는 메서드
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("handleHttpMessageNotReadableException : {}", e.getMessage());
        if (e.getCause() instanceof MismatchedInputException mismatchedInputException) {
            CausedBy causedBy = CausedBy.of(StatusCode.UNPROCESSABLE_CONTENT, ReasonCode.TYPE_MISMATCH_ERROR_IN_REQUEST_BODY);
            return ResponseEntity.unprocessableEntity().body(
                    ErrorResponse.of(
                            causedBy.getCode(),
                            causedBy.getReason(),
                            mismatchedInputException.getPath().get(0).getFieldName() + " 필드의 값이 유효하지 않습니다."
                    )
            );
        }
        CausedBy causedBy = CausedBy.of(StatusCode.BAD_REQUEST, ReasonCode.MALFORMED_REQUEST_BODY);
        return ResponseEntity.badRequest().body(
                ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage())
        );
    }

    /**
     * IllegalArgumentException이 발생한 경우
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    protected ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("handleIllegalArgumentException : {}", e.getMessage());
        e.printStackTrace();
        CausedBy causedBy = CausedBy.of(StatusCode.BAD_REQUEST, ReasonCode.INVALID_REQUEST);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }
}
