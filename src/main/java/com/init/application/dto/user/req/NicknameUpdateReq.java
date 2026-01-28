package com.init.application.dto.user.req;

import com.init.global.annotation.Nickname;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "닉네임 변경 요청 DTO")
public record NicknameUpdateReq(
        @Schema(description = "닉네임", example = "RabbitMaster")
        @Nickname
        String nickname
) {
}
