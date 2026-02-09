package com.init.application.dto.chat.res;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Slice;

import java.util.List;

@Schema(description = "채팅 메시지 목록 응답 DTO")
public record ChatMessagesRes(
        @Schema(description = "채팅 메시지 목록")
        List<ChatMessageDetailRes> messages,

        @Schema(description = "다음 페이지 존재 여부")
        boolean hasNext
) {
    public static ChatMessagesRes from(Slice<ChatMessageDetailRes> slice) {
        return new ChatMessagesRes(
                slice.getContent(),
                slice.hasNext()
        );
    }
}
