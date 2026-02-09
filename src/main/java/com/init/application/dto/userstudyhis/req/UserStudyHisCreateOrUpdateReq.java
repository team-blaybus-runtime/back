package com.init.application.dto.userstudyhis.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

@Schema(title = "사용자 학습 이력 생성 및 수정 요청 DTO")
public record UserStudyHisCreateOrUpdateReq(
        @Schema(description = "제품", example = "Drone")
        @NotNull
        String productTypeDesc,
        @Schema(description = "학습 이력 제목", example = "드론 구조 학습")
        @NotEmpty
        String title,
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
        @NotNull
        Map<String, Object> viewInfo
) {
}
