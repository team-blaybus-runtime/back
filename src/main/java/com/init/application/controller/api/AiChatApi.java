package com.init.application.controller.api;

import com.init.application.dto.chat.req.ChatReq;
import com.init.application.dto.chat.res.AiChatRes;
import com.init.global.util.CommonResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "[AI Chat API]", description = "엔지니어링 지식 기반 AI 채팅 API")
public interface AiChatApi {

    @Operation(summary = "지식 기반 AI 채팅", 
            description = "특정 제품 타입의 엔지니어링 지식을 바탕으로 AI와 채팅을 진행합니다.")
    @ApiResponse(responseCode = "200", description = "AI 답변 생성 성공")
    CommonResponseUtil<AiChatRes> chat(@RequestBody @Validated ChatReq req);
}
