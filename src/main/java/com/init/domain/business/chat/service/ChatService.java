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
import reactor.core.publisher.Flux;

import java.time.Duration;
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
    private final ApplicationEventPublisher eventPublisher;

    public AiChatRes chat(Long userId, ChatReq req) {
        // 1-4. 공통 준비 과정
        ChatPreparation preparation = prepareChat(userId, req);

        // 5. OpenAI 호출
        String answer = openAiClient.chat(preparation.currentPrompt());

        // 6. 비동기 이벤트 발행
        eventPublisher.publishEvent(
                new ChatProcessEvent(preparation.chatRoomId(), req.content(), answer)
        );

        return new AiChatRes(answer, preparation.chatRoomId());
    }

    public Flux<AiChatRes> chatStream(Long userId, ChatReq req) {
        // 1-4. 공통 준비 과정
        ChatPreparation preparation = prepareChat(userId, req);

        StringBuilder fullAnswer = new StringBuilder();
        return openAiClient.chatStream(preparation.currentPrompt())
                .bufferTimeout(10, Duration.ofMillis(100))
                .map(chunks -> {
                    String combined = String.join("", chunks);
                    fullAnswer.append(combined);
                    return new AiChatRes(combined, preparation.chatRoomId());
                })
                .doOnComplete(() -> {
                    eventPublisher.publishEvent(
                            new ChatProcessEvent(preparation.chatRoomId(), req.content(), fullAnswer.toString())
                    );
                });
    }

    private ChatPreparation prepareChat(Long userId, ChatReq req) {
        // 1. ChatRoom 확인/생성
        ChatRoom chatRoom = historyManager.getOrCreateChatRoom(userId, req.chatRoomId());

        // 2. 이전 요약 및 최근 대화 로드
        String summary = historyManager.getSummary(chatRoom.getId()).orElse(null);

        List<ChatMessage> questions = historyManager.getAllQuestions(chatRoom.getId());

        // 3. Retrieval
        List<EngineeringKnowledge> knowledges =
                retriever.retrieve(req.content(), req.productType());

        // 4. Prompt 생성
        String currentPrompt =
                promptProvider.createPrompt(req.content(), knowledges, summary, questions);

        return new ChatPreparation(chatRoom.getId(), currentPrompt);
    }

    private record ChatPreparation(Long chatRoomId, String currentPrompt) {}

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
