package com.init.application.dto.usermemo.req;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "사용자 메모 수정 요청 DTO")
public record MemoUpdateReq(
        @Schema(title = "메모 제목", example = "구조 설명")
        String title,
        @Schema(title = "메모 내용", example = "이건 이렇고, 저건 저렇고~")
        String content
) {
}
