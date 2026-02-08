package com.init.domain.business.chat.event;

import com.init.domain.business.chat.service.ChatHistoryManager;
import com.init.domain.business.chat.service.ChatSummaryService;
import com.init.domain.persistence.chat.entity.ChatMessage;
import com.init.domain.persistence.chat.entity.ChatRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatEventListener {

    private final ChatHistoryManager historyManager;
    private final ChatSummaryService summaryService;

    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 새 트랜잭션 시작
    public void handleChatProcessEvent(ChatProcessEvent event) {
        log.debug("Handling ChatProcessEvent for userHisId: {}", event.userHisId());
        try {
            historyManager.saveMessage(event.userHisId(), event.question(), ChatRole.QUESTION);
            historyManager.saveMessage(event.userHisId(), event.answer(), ChatRole.ANSWER);

            // 매 대화마다 요약본 업데이트 (현재 질문과 답변을 포함)
            List<ChatMessage> turnMessages = List.of(
                    ChatMessage.builder()
                            .userHisId(event.userHisId())
                            .content(event.question())
                            .chatRole(ChatRole.QUESTION)
                            .build(),
                    ChatMessage.builder()
                            .userHisId(event.userHisId())
                            .content(event.answer())
                            .chatRole(ChatRole.ANSWER)
                            .build()
            );

            summaryService.updateSummary(event.userHisId(), turnMessages);
        } catch (Exception e) {
            log.error("Error processing chat event for userHisId: {}", event.userHisId(), e);
        }
    }
}
