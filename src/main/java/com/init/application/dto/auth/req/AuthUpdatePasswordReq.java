package com.init.application.dto.auth.req;

import com.init.global.annotation.Password;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "비밀번호 변경 요청 DTO")
public record AuthUpdatePasswordReq(
        @Schema(title = "기존 비밀번호", example = "oldPassword1234")
        @Password
        String oldPassword,
        @Schema(title = "새로운 비밀번호", example = "NewPassword1234")
        @Password
        String newPassword
) {
}
