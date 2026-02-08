package com.init.application.dto.auth.req;

import com.init.global.annotation.Email;
import com.init.global.annotation.Password;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "일반 회원가입 요청 DTO")
public record AuthSignUpReq(
        @Schema(title = "이메일", example = "admin@naver.com")
        @Email
        String email,
        @Schema(title = "비밀번호", example = "admin1234")
        @Password
        String password,
        @Schema(title = "확인 비밀번호", example = "admin1234")
        @Password
        String confirmPassword
) {
}
