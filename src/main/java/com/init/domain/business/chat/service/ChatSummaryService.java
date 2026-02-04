package com.init.domain.business.chat.service;

import com.init.domain.persistence.chat.entity.ChatMessage;
import com.init.domain.persistence.chat.entity.ChatRole;
import com.init.domain.persistence.chat.entity.ChatRoom;
import com.init.domain.persistence.chat.entity.ChatSummary;
import com.init.domain.persistence.chat.repository.ChatRoomRepository;
import com.init.domain.persistence.chat.repository.ChatSummaryRepository;
import com.init.infra.openai.client.OpenAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatSummaryService {

    private final OpenAiClient openAiClient;
    private final ChatSummaryRepository chatSummaryRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SummaryPromptProvider summaryPromptProvider;

    @Transactional
    public void updateSummary(Long chatRoomId, List<ChatMessage> recentMessages) {
        Optional<ChatSummary> optionalSummary =
                chatSummaryRepository.findByChatRoomId(chatRoomId);

        if (optionalSummary.isEmpty()) {
            createFirstSummary(chatRoomId, recentMessages);
            return;
        }

        updateExistingSummary(optionalSummary.get(), recentMessages);
    }

    private void createFirstSummary(Long chatRoomId, List<ChatMessage> messages) {
        String prompt = summaryPromptProvider.buildFirstSummaryPrompt(messages);
        String summary = openAiClient.chat(prompt);
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();

        chatSummaryRepository.save(
                ChatSummary.builder()
                        .chatRoom(chatRoom)
                        .summary(summary)
                        .build()
        );
    }

    private void updateExistingSummary(ChatSummary summary, List<ChatMessage> messages) {
        String prompt = summaryPromptProvider.buildCumulativeSummaryPrompt(summary.getSummary(), messages);
        String updatedSummary = openAiClient.chat(prompt);
        summary.setSummary(updatedSummary);
    }
}
