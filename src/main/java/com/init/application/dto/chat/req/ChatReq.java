package com.init.application.dto.chat.req;

import com.init.domain.persistence.engineering.entity.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AI 채팅 요청 DTO")
public record ChatReq(
        @Schema(description = "질문 내용", example = "드론의 프로펠러 재질은 무엇인가요? 테스트")
        String content,

        @Schema(description = "제품 타입", example = "Drone")
        ProductType productType,

        @Schema(description = "채팅방 ID (기존 대화 이어하기용, 없을 경우 새로 생성)", example = "1")
        Long chatRoomId,

        Long userId
) {
}
