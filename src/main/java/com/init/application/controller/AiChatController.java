package com.init.application.controller;

import com.init.application.controller.api.AiChatApi;
import com.init.application.dto.chat.req.ChatHistoryReq;
import com.init.application.dto.chat.req.ChatReq;
import com.init.application.dto.chat.res.AiChatRes;
import com.init.application.dto.chat.res.ChatMessagesRes;
import com.init.domain.business.chat.service.ChatService;
import com.init.infra.security.authentication.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ai-chat")
@RestController
public class AiChatController implements AiChatApi {

    private final ChatService chatService;

    @Override
    public AiChatRes chat(@AuthenticationPrincipal SecurityUserDetails user, @RequestBody @Validated ChatReq req){
        return chatService.chat(user.getUserId(), req);
    }

    @Override
    public Flux<AiChatRes> chatStream(@AuthenticationPrincipal SecurityUserDetails user,@RequestBody @Validated ChatReq req) {
        return chatService.chatStream(user.getUserId(), req);
    }

    @Override
    public ChatMessagesRes getChatHistory(
            @AuthenticationPrincipal SecurityUserDetails user,
            @PathVariable Long historyId,
            ChatHistoryReq pageReq // @RequestParam 없이 객체로 선언
    ) {
        return chatService.getChatHistory(
                user.getUserId(),
                historyId,
                pageReq.lastId(),
                pageReq.order(),
                pageReq.limit()
        );
    }
}
