package com.init.domain.business.chat.event;

import com.init.domain.business.chat.service.ChatHistoryManager;
import com.init.domain.business.chat.service.ChatSummaryService;
import com.init.domain.persistence.chat.entity.ChatRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

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

            historyManager
                    .getMessagesForSummaryUpdate(event.chatRoomId())
                    .ifPresent(m ->
                            summaryService.updateSummary(event.chatRoomId(), m)
                    );
        } catch (Exception e) {
            log.error("Error processing chat event for chatRoomId: {}", event.chatRoomId(), e);
        }
    }
}
