package com.init.application.dto.engineering.res;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "엔지니어링 부품 조회 응답 DTO")
public record EngineeringPartReadRes(
        @Schema(title = "부품 이름", example = "Arm Gear")
        String partName,
        @Schema(title = "부품 설명", example = "로봇 팔의 주요 기어 부품입니다.")
        String content,
        @Schema(title = "부품 이미지 URL", example = "http://example.com/images/arm_gear.png")
        String imageUrl
) {
}
