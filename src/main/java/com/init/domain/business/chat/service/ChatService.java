package com.init.domain.business.chat.service;

import com.init.domain.persistence.engineering.entity.EngineeringKnowledge;
import com.init.domain.persistence.engineering.entity.ProductType;
import com.init.infra.openai.client.OpenAiClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiClient openAiClient;
    private final EngineeringKnowledgeRetriever retriever;
    private final EngineeringChatPromptProvider promptProvider;

    public String chat(String question, ProductType productType) {
        // 1. 관련 지식 검색 (Retrieval)
        List<EngineeringKnowledge> knowledges = retriever.retrieve(question, productType);
        log.debug("Found {} relevant knowledges for productType: {}", knowledges.size(), productType);

        // 2. 프롬프트 생성 (Prompt Construction)
        String currentPrompt = promptProvider.createPrompt(question, knowledges);

        // 3. OpenAI 호출 (Generation)
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "user", "content", currentPrompt));

        return openAiClient.chat(messages);
    }
}
