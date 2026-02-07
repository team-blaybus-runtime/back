package com.init.application.dto.auth.req;

import com.init.global.annotation.Password;
import com.init.global.annotation.Username;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "일반 회원가입 요청 DTO")
public record AuthSignUpReq(
        @Schema(title = "아이디", example = "admin")
        @Username
        String username,
        @Schema(title = "비밀번호", example = "admin1234")
        @Password
        String password,
        @Schema(title = "확인 비밀번호", example = "admin1234")
        @Password
        String confirmPassword
) {
}
