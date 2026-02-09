package com.init.application.controller.api;

import com.init.application.dto.chat.req.ChatHistoryReq;
import com.init.application.dto.chat.req.ChatReq;
import com.init.application.dto.chat.res.AiChatRes;
import com.init.application.dto.chat.res.ChatMessagesRes;
import com.init.infra.security.authentication.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;

@Tag(name = "[AI Chat API]", description = "엔지니어링 지식 기반 AI 채팅 API")
public interface AiChatApi {

    @Operation(summary = "지식 기반 AI 채팅",
            description = "특정 제품 타입의 엔지니어링 지식을 바탕으로 AI와 채팅을 진행합니다.")
    @ApiResponse(responseCode = "200", description = "AI 답변 생성 성공")
    @PostMapping
    AiChatRes chat(@AuthenticationPrincipal SecurityUserDetails user,@RequestBody @Validated ChatReq req);

    @Operation(summary = "지식 기반 AI 채팅 (스트리밍)",
            description = "특정 제품 타입의 엔지니어링 지식을 바탕으로 AI와 채팅을 진행하며, 답변을 스트리밍 방식으로 수신합니다.")
    @ApiResponse(responseCode = "200", description = "AI 답변 스트리밍 시작")
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<AiChatRes> chatStream(@AuthenticationPrincipal SecurityUserDetails user,@RequestBody @Validated ChatReq req);

    @Operation(
            summary = "AI 채팅 목록 조회 (무한 스크롤)",
            description = """
AI 채팅 기록을 **무한 스크롤 방식**으로 조회합니다.
마지막으로 조회된 메시지 ID를 기준으로 **이전 메시지**를 조회합니다.

### 요청 파라미터 설명

- **lastId** (선택)
  - 마지막으로 조회된 메시지 ID
  - 해당 ID **이전 메시지부터 조회**
  - 최초 조회 시 `null` 가능

- **order** (선택)
  - 정렬 순서
  - 가능한 값: `asc`, `desc`
  - 기본값: `desc`

- **limit** (선택)
  - 한 번에 조회할 메시지 개수
  - 기본값: `10`
"""
    )
    @ApiResponse(responseCode = "200", description = "AI 채팅 목록 조회 성공")
    @GetMapping("{historyId}")
    ChatMessagesRes getChatHistory(@AuthenticationPrincipal SecurityUserDetails user, @PathVariable Long historyId, @Validated ChatHistoryReq pageReq);
}
