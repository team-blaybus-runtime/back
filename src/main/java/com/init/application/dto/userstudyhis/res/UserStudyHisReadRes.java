package com.init.application.dto.userstudyhis.res;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "사용자 학습 이력 조회 응답 DTO")
public record UserStudyHisReadRes(
        @Schema(description = "사용자 학습 이력 ID", example = "1")
        Long userStudyHisId,
        @Schema(description = "학습 이력 제목", example = "드론 구조 학습")
        String title,
        @Schema(description = "수정 일시", example = "2024-01-01T12:00:00")
        LocalDateTime updatedAt,
        @Schema(
                description = "유저의 뷰 정보 (JSON)",
                example = """
                        {
                          "partId": 1,
                          "position": [0, 0, 0],
                          "geometry": [1, 1.5, 0.6],
                          "color": "#6366f1",
                          "roughnessMultiplier": 1,
                          "metalnessMultiplier": 1,
                          "envMapMultiplier": 1,
                          "roughness": 0.4,
                          "metalness": 0.6,
                          "envMapIntensity": 1.0
                        }
                        """
        )
        Object viewInfo,

        @Schema(description = "제품", example = "Drone")
        String ProductTypeDesc,
        @Schema(description = "제품 이미지 URL", example = "https://example.com/images/drone.png")
        String productImageUrl
) {
}
