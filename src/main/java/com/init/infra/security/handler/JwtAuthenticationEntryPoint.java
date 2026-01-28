package com.init.infra.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.init.global.exception.payload.CausedBy;
import com.init.global.exception.payload.ErrorResponse;
import com.init.global.exception.payload.ReasonCode;
import com.init.global.exception.payload.StatusCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.warn("commence error: {}, request method: {}, request url: {}", authException.getMessage(), request.getMethod(), request.getRequestURI());
        CausedBy causedBy = CausedBy.of(StatusCode.UNAUTHORIZED, ReasonCode.MISSING_OR_INVALID_AUTHENTICATION_CREDENTIALS);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(causedBy.statusCode().getCode());
        ErrorResponse errorResponse = ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), authException.getMessage());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
