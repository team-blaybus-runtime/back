package com.init.domain.business.chat.event;

import com.init.domain.business.chat.service.ChatHistoryManager;
import com.init.domain.business.chat.service.ChatSummaryService;
import com.init.domain.persistence.chat.entity.ChatMessage;
import com.init.domain.persistence.chat.entity.ChatRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatEventListener {

    private final ChatHistoryManager historyManager;
    private final ChatSummaryService summaryService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChatProcessEvent(ChatProcessEvent event) {
        log.debug("Handling ChatProcessEvent for chatRoomId: {}", event.chatRoomId());
        try {
            historyManager.saveMessage(event.chatRoomId(), event.question(), ChatRole.QUESTION);
            historyManager.saveMessage(event.chatRoomId(), event.answer(), ChatRole.ANSWER);

            // 매 대화마다 요약본 업데이트 (현재 질문과 답변을 포함)
            List<ChatMessage> turnMessages = List.of(
                    ChatMessage.builder()
                            .chatRoomId(event.chatRoomId())
                            .content(event.question())
                            .chatRole(ChatRole.QUESTION)
                            .build(),
                    ChatMessage.builder()
                            .chatRoomId(event.chatRoomId())
                            .content(event.answer())
                            .chatRole(ChatRole.ANSWER)
                            .build()
            );

            summaryService.updateSummary(event.chatRoomId(), turnMessages);
        } catch (Exception e) {
            log.error("Error processing chat event for chatRoomId: {}", event.chatRoomId(), e);
        }
    }
}
