package com.init.application.dto.chat.res;

import com.init.domain.persistence.chat.entity.ChatRole;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "채팅 메시지 상세 응답 DTO")
public record ChatMessageDetailRes(
        @Schema(description = "메시지 내용")
        String content,

        @Schema(description = "메시지 역할 (QUESTION/ANSWER)")
        ChatRole role,

        @Schema(description = "생성 일시")
        LocalDateTime createdAt
) {
}
