package com.init.domain.business.chat.service;

import com.init.application.dto.chat.req.ChatReq;
import com.init.application.dto.chat.res.AiChatRes;
import com.init.domain.persistence.chat.entity.ChatMessage;
import com.init.domain.persistence.chat.entity.ChatRole;
import com.init.domain.persistence.chat.entity.ChatRoom;
import com.init.domain.persistence.engineering.entity.EngineeringKnowledge;
import com.init.infra.openai.client.OpenAiClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiClient openAiClient;
    private final EngineeringKnowledgeRetriever retriever;
    private final EngineeringChatPromptProvider promptProvider;
    private final ChatHistoryManager historyManager;
    private final ChatSummaryService summaryService;

    public AiChatRes chat(Long userId, ChatReq req) {
        // 1. ChatRoom 확인/생성
        ChatRoom chatRoom = historyManager.getOrCreateChatRoom(userId, req.chatRoomId());

        // 2. 이전 요약 및 최근 대화 로드 (현재 질문 저장 전의 이력)
        String summary = historyManager.getSummary(chatRoom.getId()).orElse(null);
        List<ChatMessage> history = historyManager.getRecentMessages(chatRoom.getId());

        // 3. 사용자 질문 저장
        historyManager.saveMessage(chatRoom.getId(), req.content(), ChatRole.QUESTION);

        // 4. 관련 지식 검색 (Retrieval)
        List<EngineeringKnowledge> knowledges = retriever.retrieve(req.content(), req.productType());

        // 5. 프롬프트 생성 (Prompt Construction) - 요약과 이전 이력 포함
        String currentPrompt = promptProvider.createPrompt(req.content(), knowledges, summary, history);

        // 6. OpenAI 호출 (Generation)
        String answer = openAiClient.chat(currentPrompt);

        // todo 추후 성능 개선을 통해 이벤트 방식을 통한 비동기 저장 사용시 성능 향상 가능
        // 7. AI 응답 저장
        historyManager.saveMessage(chatRoom.getId(), answer, ChatRole.ANSWER);

        // 8. 요약 갱신 여부 판단 (메시지 개수가 10개 단위일 때)
        historyManager
                .getMessagesForSummaryUpdate(chatRoom.getId())
                .ifPresent(m ->
                        summaryService.updateSummary(chatRoom.getId(), m)
                );


        return new AiChatRes(answer, chatRoom.getId());
    }
}
