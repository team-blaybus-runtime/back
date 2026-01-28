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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.warn("handle error: {}", accessDeniedException.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.FORBIDDEN, ReasonCode.ACCESS_TO_THE_REQUESTED_RESOURCE_IS_FORBIDDEN);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(causedBy.statusCode().getCode());
        ErrorResponse errorResponse = ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), accessDeniedException.getMessage());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
