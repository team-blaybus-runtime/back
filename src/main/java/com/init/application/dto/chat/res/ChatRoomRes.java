package com.init.application.dto.chat.res;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "채팅방 목록 응답 DTO")
public record ChatRoomRes(
        @Schema(description = "채팅방 ID")
        Long chatRoomId,

        @Schema(description = "생성 일시")
        LocalDateTime createdAt
) {
}
