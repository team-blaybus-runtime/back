package com.init.application.dto.chat.res;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AI 채팅 응답 DTO")
public record AiChatRes(
        @Schema(description = "AI 답변 내용")
        String answer

) {
    public AiChatRes(String answer) {
        this.answer = answer;
    }
}
