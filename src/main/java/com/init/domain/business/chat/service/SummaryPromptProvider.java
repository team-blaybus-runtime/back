package com.init.domain.business.chat.service;

import com.init.domain.persistence.chat.entity.ChatMessage;
import com.init.global.annotation.Helper;
import com.init.global.util.ChatPromptUtils;

import java.util.List;

/**
 * 대화 요약 프롬프트 생성을 담당하는 서비스 클래스.
 */
@Helper
public class SummaryPromptProvider {
    private static final String FIRST_SUMMARY_PREFIX =
            "Summarize the following conversation with a focus on the technical context.\n\n";

    private static final String CUMULATIVE_SUMMARY_TMPL = """
            The following is an existing conversation summary.
            Update this summary by incorporating the new conversation while preserving the existing content.

            [Existing Summary]
            %s

            [New Conversation]
            %s
            """;

    /**
     * 대화 요약 프롬프트를 생성하여 반환.
     *
     * @param messages 대화 메시지 리스트
     * @return 생성된 대화 요약 프롬프트 문자열
     */
    public String buildFirstSummaryPrompt(List<ChatMessage> messages) {
        return FIRST_SUMMARY_PREFIX + ChatPromptUtils.formatChatHistory(messages);
    }

    /**
     * 기존 요약과 새로운 대화를 기반으로 누적된 대화 요약 프롬프트를 생성하여 반환.
     *
     * @param oldSummary 기존 대화 요약 문자열
     * @param messages 새로운 대화 메시지 리스트
     * @return 생성된 누적 대화 요약 프롬프트 문자열
     */
    public String buildCumulativeSummaryPrompt(String oldSummary, List<ChatMessage> messages) {
        return String.format(
                CUMULATIVE_SUMMARY_TMPL,
                oldSummary,
                ChatPromptUtils.formatChatHistory(messages)
        );
    }
}
