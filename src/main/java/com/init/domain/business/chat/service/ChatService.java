package com.init.domain.business.chat.service;

import com.init.application.dto.chat.req.ChatReq;
import com.init.application.dto.chat.res.AiChatRes;
import com.init.domain.business.chat.event.ChatProcessEvent;
import com.init.domain.persistence.chat.entity.ChatMessage;
import com.init.domain.persistence.chat.entity.ChatRoom;
import com.init.domain.persistence.engineering.entity.EngineeringKnowledge;
import com.init.infra.openai.client.OpenAiClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiClient openAiClient;
    private final EngineeringKnowledgeRetriever retriever;
    private final EngineeringChatPromptProvider promptProvider;
    private final ChatQueryRewriter queryRewriter;
    private final ChatHistoryManager historyManager;
    private final ApplicationEventPublisher eventPublisher;

    public AiChatRes chat(Long userId, ChatReq req) {
        // 1. ChatRoom 확인/생성
        ChatRoom chatRoom = historyManager.getOrCreateChatRoom(userId, req.chatRoomId());

        // 2. 이전 요약 및 최근 대화 로드 (현재 질문 저장 전의 이력)
        String summary = historyManager.getSummary(chatRoom.getId()).orElse(null);
        List<ChatMessage> history = historyManager.getRecentMessages(chatRoom.getId());

        // 3. 질문 재작성 (Contextual Query Expansion) - retrieval을 위해 문맥 반영
        String searchContext = queryRewriter.rewrite(req.content(), summary, history);

        // 4. 관련 지식 검색 (Retrieval) - 재작성된 쿼리 사용
        List<EngineeringKnowledge> knowledges = retriever.retrieve(searchContext, req.productType());

        // 5. 프롬프트 생성 (Prompt Construction) - 요약과 이전 이력 포함
        String currentPrompt = promptProvider.createPrompt(req.content(), knowledges, summary, history);

        // 6. OpenAI 호출 (Generation)
        String answer = openAiClient.chat(currentPrompt);

        // todo 결합도를 약하게 하기 위해서 비돋시 이벤트를 통한 데이터 저장.. 추후에 mq,kafka가 들어갈때 해당 부분을 수정 필요?
        // 6. 비동기 프로세스 이벤트 발행 (메시지 저장 및 요약 갱신)
        eventPublisher.publishEvent(new ChatProcessEvent(chatRoom.getId(), req.content(), answer));

        return new AiChatRes(answer, chatRoom.getId());
    }
}
