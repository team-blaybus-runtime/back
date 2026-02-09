package com.init.application.dto.usermemo.res;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(title = "사용자 메모 조회 응답 DTO")
public record MemoReadRes(
        @Schema(title = "메모 ID", example = "1")
        Long memoId,
        @Schema(title = "제품 유형", example = "Drone")
        String productTypeDesc,
        @Schema(title = "메모 제목", example = "구조 설명")
        String title,
        @Schema(title = "메모 내용", example = "이건 이렇고, 저건 저렇고~")
        String content,
        @Schema(title = "수정된 시간", example = "2024-01-01T12:00:00")
        LocalDateTime updatedAt
) {
}
