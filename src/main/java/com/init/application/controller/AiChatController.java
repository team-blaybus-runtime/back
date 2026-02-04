package com.init.application.controller;

import com.init.application.controller.api.AiChatApi;
import com.init.application.dto.chat.req.ChatReq;
import com.init.application.dto.chat.res.AiChatRes;
import com.init.domain.business.chat.service.ChatService;
import com.init.global.util.CommonResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ai-chat")
@RestController
public class AiChatController implements AiChatApi {

    private final ChatService chatService;

    // todo 업데이트 진행시에 실제 AuthenticationPrincipal 을 통해서 사용자의 Id를 가져오 도록 수정 필요
    @PostMapping
    public CommonResponseUtil<AiChatRes> chat(@RequestBody @Validated ChatReq req){
        AiChatRes res = chatService.chat(req.userId(), req);
        return CommonResponseUtil.ok(res);
    }
}
