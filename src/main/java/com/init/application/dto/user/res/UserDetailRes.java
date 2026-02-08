package com.init.application.dto.user.res;

import com.init.domain.persistence.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "회원 상세정보 응답 DTO")
public record UserDetailRes(
        @Schema(title = "유저 ID", example = "1")
        Long userId,
        @Schema(title = "닉네임", example = "도사")
        String nickname,
        @Schema(title = "아이디", example = "admin@naver.com")
        String username,
        @Schema(title = "비밀번호", example = "admin1234")
        String password,
        @Schema(title = "전공", example = "컴퓨터공학과")
        String major,
        @Schema(title = "학년", example = "3")
        Integer grade,
        @Schema(title = "목표", example = "드론 구조 완벽히 이해하기")
        String goal,
        @Schema(title = "회원 권한", example = "USER")
        Role role
) {
}
