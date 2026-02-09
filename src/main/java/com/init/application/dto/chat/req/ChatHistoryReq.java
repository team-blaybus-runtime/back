package com.init.application.dto.chat.req;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "채팅 내역 조회 요청 파라미터")
public record ChatHistoryReq(
        @Schema(description = "마지막으로 조회된 메시지 ID")
        Long lastId,

        @Schema(description = "정렬 순서 (asc/desc)", defaultValue = "desc")
        String order,

        @Schema(description = "조회 개수", defaultValue = "10")
        Integer limit
) {
    // 생성자를 통해 기본값을 보장할 수도 있습니다.
    public ChatHistoryReq {
        if (order == null) order = "desc";
        if (limit == null) limit = 10;
    }
}
