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
            // 1. 메시지 저장
            historyManager.saveMessage(event.userHisId(), event.question(), ChatRole.QUESTION);
            historyManager.saveMessage(event.userHisId(), event.answer(), ChatRole.ANSWER);

            // 2. 메시지 개수 확인
            long count = historyManager.getMessageCount(event.userHisId());

            // 3. 요약 업데이트 전략 (최적화)
            if (count == 2) {
                summaryService.updateSummary(event.userHisId(), historyManager.getRecentMessages(event.userHisId(), 2));
            } else if (count > 2 && count % 6 == 0) {
                // 최근 2턴(4개 메시지)을 가져와서 기존 요약에 추가
                summaryService.updateSummary(event.userHisId(), historyManager.getRecentMessages(event.userHisId(), 4));
            }

        } catch (Exception e) {
            log.error("Error processing chat event for userHisId: {}", event.userHisId(), e);
        } finally {
            log.debug("ChatProcessEvent handled for userHisId: {}", event.userHisId());
        }
    }
}
