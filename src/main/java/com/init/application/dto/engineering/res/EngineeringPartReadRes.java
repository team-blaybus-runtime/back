package com.init.application.dto.engineering.res;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(title = "엔지니어링 부품 조회 응답 DTO")
public record EngineeringPartReadRes(
        @Schema(title = "부품 이름", example = "Arm Gear")
        String partName,
        @Schema(title = "부품 설명", example = "로봇 팔의 주요 기어 부품입니다.")
        String content,
        @Schema(title = "초기 좌표 정보", example = "{\"x\":1.125,\"y\":2.724652,\"z\":7.12313}")
        Map<String, Object> position,
        @Schema(title = "부품 에셋 파일 URL", example = "http://example.com/assets/arm_gear.glb")
        String assetUrl,
        @Schema(title = "부품 이미지 URL", example = "http://example.com/images/arm_gear.png")
        String imageUrl
) {
}
