package com.init.domain.business.chat.service;

import com.init.application.dto.chat.req.ChatReq;
import com.init.application.dto.chat.res.AiChatRes;
import com.init.application.dto.chat.res.ChatMessageDetailRes;
import com.init.application.dto.chat.res.ChatRoomRes;
import com.init.domain.business.chat.event.ChatProcessEvent;
import com.init.domain.persistence.chat.entity.ChatMessage;
import com.init.domain.persistence.chat.entity.ChatRoom;
import com.init.domain.persistence.engineering.entity.EngineeringKnowledge;
import com.init.global.util.StepTimer;
import com.init.infra.openai.client.OpenAiClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ChatQueryRewriter queryRewriter;
    private final ChatHistoryManager historyManager;
    private final ApplicationEventPublisher eventPublisher;

    public AiChatRes chat(Long userId, ChatReq req) {
        StepTimer timer = new StepTimer("AI_CHAT");

        // 1. ChatRoom 확인/생성
        ChatRoom chatRoom = historyManager.getOrCreateChatRoom(userId, req.chatRoomId());
        timer.log("1. getOrCreateChatRoom");

        // 2. 이전 요약 및 최근 대화 로드
        String summary = historyManager.getSummary(chatRoom.getId()).orElse(null);
        timer.log("2-1. getSummary");

        List<ChatMessage> questions = historyManager.getAllQuestions(chatRoom.getId());
        timer.log("2-2. getAllQuestions");

        // 3. Retrieval
        List<EngineeringKnowledge> knowledges =
                retriever.retrieve(req.content(), req.productType());
        timer.log("3. retrieve");

        // 4. Prompt 생성
        String currentPrompt =
                promptProvider.createPrompt(req.content(), knowledges, summary, questions);
        timer.log("4. createPrompt");
//        log.info("프롬포트는 : {}", currentPrompt);

        // 5. OpenAI 호출
        String answer = openAiClient.chat(currentPrompt);
        timer.log("5. openAiChat");

        // 6. 비동기 이벤트 발행
        eventPublisher.publishEvent(
                new ChatProcessEvent(chatRoom.getId(), req.content(), answer)
        );
        timer.log("6. publishEvent");

        return new AiChatRes(answer, chatRoom.getId());
    }

    public List<ChatRoomRes> getChatRooms(Long userId) {
        return historyManager.getChatRoomsByUserId(userId).stream()
                .map(room -> new ChatRoomRes(room.getId(), room.getCreatedAt()))
                .toList();
    }

    public List<ChatMessageDetailRes> getChatMessages(Long chatRoomId) {
        return historyManager.getChatMessagesByChatRoomId(chatRoomId).stream()
                .map(msg -> new ChatMessageDetailRes(msg.getContent(), msg.getChatRole(), msg.getCreatedAt()))
                .toList();
    }
}
