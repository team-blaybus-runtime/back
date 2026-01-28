package com.init.global.advice;

import com.init.global.exception.GlobalException;
import com.init.global.exception.payload.CausedBy;
import com.init.global.exception.payload.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionAdvice {

    /**
     * 비즈니스 로직 수행 중 개발자가 직접 정의한 예외를 처리하는 메서드
     */
    @ExceptionHandler(GlobalException.class)
    protected ResponseEntity<ErrorResponse> handleGlobalException(GlobalException e) {
        log.warn("handleGlobalException : {}", e.getMessage());
        CausedBy causedBy = e.causedBy();
        ErrorResponse response = ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getExplainError());

        return ResponseEntity.status(causedBy.statusCode().getCode()).body(response);
    }
}
