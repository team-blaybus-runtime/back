package com.init.application.dto.engineering.res;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "엔지니어 제품 조회 응답 DTO")
public record EngineeringReadRes(
        @Schema(title = "제품 유형 설명", example = "Drone")
        String productTypeDesc,
        @Schema(title = "제품 이미지 URL", example = "https://example.com/images/drone.png")
        String imageUrl
) {
}
