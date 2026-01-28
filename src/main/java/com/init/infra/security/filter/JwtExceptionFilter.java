package com.init.infra.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.init.global.exception.GlobalException;
import com.init.global.exception.payload.CausedBy;
import com.init.global.exception.payload.ErrorResponse;
import com.init.global.util.JwtErrorCodeUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.warn("Exception caught in JwtExceptionFilter: {}", e.getMessage());
            GlobalException exception = JwtErrorCodeUtil.determineAuthErrorException(e);

            sendAuthError(response, exception);
        }
    }

    private void sendAuthError(HttpServletResponse response, GlobalException e) throws IOException {
        CausedBy causedBy = e.causedBy();

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(causedBy.statusCode().getCode());
        ErrorResponse errorResponse = ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getExplainError());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
