package com.init.application.dto.user.res;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "로그인 성공 응답 DTO")
public record AuthenticatedUserRes(
        @Schema(title = "유저 ID", example = "1")
        Long userId,
        @Schema(title = "유저 역할", example = "ROLE_ADMIN, ROLE_USER, ROLE_GUEST")
        String roleType,
        @Schema(title = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,
        @Schema(title = "리프레시 토큰", example = "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...")
        String refreshToken,
        @Schema(title = "토큰 타입", example = "Bearer")
        String tokenType
) {
}
