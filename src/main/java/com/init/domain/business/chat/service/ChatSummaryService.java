package com.init.domain.business.chat.service;

import com.init.domain.business.userstudyhis.service.UserStudyHisService;
import com.init.domain.persistence.chat.entity.ChatMessage;
import com.init.domain.persistence.chat.entity.ChatSummary;
import com.init.domain.persistence.chat.repository.ChatSummaryRepository;
import com.init.domain.persistence.userstudyhis.entity.UserStudyHis;
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
    private final SummaryPromptProvider summaryPromptProvider;
    private final UserStudyHisService userStudyHisService;

    @Transactional
    public void updateSummary(Long userHisId, List<ChatMessage> recentMessages) {
        Optional<ChatSummary> optionalSummary =
                chatSummaryRepository.findByUserStudyHisId(userHisId);

        if (optionalSummary.isEmpty()) {
            createFirstSummary(userHisId, recentMessages);
            return;
        }

        updateExistingSummary(optionalSummary.get(), recentMessages);
    }

    private void createFirstSummary(Long userHisId, List<ChatMessage> messages) {
        String prompt = summaryPromptProvider.buildFirstSummaryPrompt(messages);
        String summary = openAiClient.chat(prompt);
        UserStudyHis userHistory = userStudyHisService.getUserHistoryId(userHisId);

        chatSummaryRepository.save(
                ChatSummary.builder()
                        .userStudyHis(userHistory)
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
