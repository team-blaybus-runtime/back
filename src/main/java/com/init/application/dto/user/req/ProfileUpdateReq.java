package com.init.application.dto.user.req;

import com.init.global.annotation.Nickname;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(title = "프로필 수정 요청 DTO")
public record ProfileUpdateReq(
        @Schema(title = "닉네임", example = "홍길동")
        @Nickname
        String nickname,
        @Schema(description = "전공", example = "컴퓨터공학과")
        String major,
        @Schema(description = "학년", example = "3")
        @Min(1)
        @Max(10)
        Integer grade,
        @Schema(description = "목표", example = "드론 동작 원리 100% 이해하기")
        String goal
) {
}
