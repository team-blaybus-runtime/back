package com.init.domain.business.chat.service;

import com.init.domain.persistence.chat.entity.ChatMessage;
import com.init.global.annotation.Helper;
import com.init.global.util.ChatPromptUtils;
import com.init.infra.openai.client.OpenAiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 대화 문맥을 바탕으로 사용자의 질문을 독립적인 검색 쿼리로 재작성하는 헬퍼 클래스.
 */
@Slf4j
@Helper
@RequiredArgsConstructor
public class ChatQueryRewriter {

    private final OpenAiClient openAiClient;

    private static final String REWRITE_PROMPT_TMPL = """
        Given the following conversation history and a follow-up question, rephrase the follow-up question to be a standalone search query that can be used to find relevant technical information.
        The goal is to resolve pronouns (like "that part", "it", "there") and context based on the previous conversation.
        If the question is already standalone, return it as is.

        [Previous Conversation Summary]
        %s

        [Recent Conversation History]
        %s

        [Follow-up Question]
        %s

        Standalone Query:
        """;

    /**
     * 질문을 문맥에 맞게 재작성합니다.
     *
     * @param question 사용자의 현재 질문
     * @param summary 이전 대화 요약
     * @param history 최근 대화 내역
     * @return 재작성된 독립적 쿼리
     */
    public String rewrite(String question, String summary, List<ChatMessage> history) {
        if ((summary == null || summary.isEmpty()) && (history == null || history.isEmpty())) {
            return question;
        }

        String historyText = ChatPromptUtils.formatChatHistory(history);
        String prompt = String.format(REWRITE_PROMPT_TMPL,
                (summary != null && !summary.isEmpty()) ? summary : "None",
                (historyText != null && !historyText.isEmpty()) ? historyText : "None",
                question);

        return openAiClient.chat(prompt).trim();
    }
}
