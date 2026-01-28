package com.init.application.dto.auth.req;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "JWT 토큰 재발급 DTO")
public record AuthRefreshReq(
        @Schema(title = "리프레시 토큰",
                description = "재발급에 사용할 유효한 리프레시 토큰",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String refreshToken
) {
}
