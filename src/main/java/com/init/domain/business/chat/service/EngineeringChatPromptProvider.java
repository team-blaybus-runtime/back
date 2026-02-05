package com.init.domain.business.chat.service;

import com.init.domain.persistence.chat.entity.ChatMessage;
import com.init.domain.persistence.chat.entity.ChatRole;
import com.init.domain.persistence.engineering.entity.EngineeringKnowledge;
import com.init.global.annotation.Helper;
import com.init.global.util.ChatPromptUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 엔지니어링 관련 채팅 프롬프트 생성을 담당하는 서비스 클래스.
 */
@Helper
public class EngineeringChatPromptProvider {


    /**
     * 사용자 질문과 엔지니어링 지식을 기반으로 메시지 생성 템플릿을 반환.
     *
     * @param question 사용자가 입력한 질문 문자열
     * @param knowledges 엔지니어링 지식 리스트
     * @param summary 이전 대화 요약 문자열
     * @param history 최근 대화 내역 리스트
     * @return 사용자 질문과 컨텍스트 기반으로 생성된 템플릿 문자열
     */
    public String createPrompt(String question, List<EngineeringKnowledge> knowledges, String summary, List<ChatMessage> history) {
        StringBuilder contextWithHistory = new StringBuilder(formatContext(knowledges));

        if (summary != null && !summary.isEmpty()) {
            contextWithHistory.append("\n\n[Previous Conversation Summary]\n").append(summary);
        }

        if (history != null && !history.isEmpty()) {
            contextWithHistory.append("\n\n[Recent Conversation History]\n");
            contextWithHistory.append(ChatPromptUtils.formatChatHistory(history));
        }

        return getTemplate().formatted(contextWithHistory.toString(), question);
    }

    /**
     * 엔지니어링 지식 리스트를 포맷된 문자열로 변환.
     *
     * @param knowledges 포맷 대상 엔지니어링 지식 리스트
     * @return 각 엔지니어링 지식을 포맷한 문자열을 개별 항목마다 두 줄의 개행으로 구분한 결과 문자열
     */
    private String formatContext(List<EngineeringKnowledge> knowledges) {
        return knowledges.stream()
                .map(this::formatSingleKnowledge)
                .collect(Collectors.joining("\n\n"));
    }

    /**
     * 엔지니어링 지식을 포맷된 문자열로 변환.
     *
     * @param p 포맷 대상 엔지니어링 지식
     * @return 포맷된 엔지니어링 지식 문자열
     */
    private String formatSingleKnowledge(EngineeringKnowledge p) {
        return """
[Product]
- Product Type: %s
- Category: %s
- Equipment: %s

[Component]
- System: %s
- Part Name (KO/EN): %s

[Material]
- %s

[Description]
%s
""".formatted(
                p.getProductType(),
                p.getCategory(),
                p.getEquipment(),
                p.getSystemClassification(),
                p.getPartName(),
                p.getMaterial(),
                p.getContent()
        );
    }

    /**
     * 엔지니어링 지식 리스트를 포맷된 문자열로 변환.
     *
     * @return 각 엔지니어링 지식을 포맷한 문자열을 개별 항목마다 두 줄의 개행으로 구분한 결과 문자열
     */
    private String getTemplate() {
        return """
You are a senior mechanical and systems engineer.

Your task is to answer the user's question using ONLY the provided engineering context.
You must follow all rules strictly.

[Answering Rules]
1. Base your answer strictly on the given context (Search Results, Previous Summary, and Recent History). Do NOT assume or invent information.
2. Use precise engineering terminology.
3. When referring to components, ALWAYS show the name in both Korean and English.
   - Format: Korean Name (English Name)
   - Example: 코일 스프링 (Coil Spring)
4. Preserve original technical names exactly as provided in the context.
5. If multiple components are involved, explain their relationship clearly.
6. Structure your answer logically:
   - Overview
   - Operating principle
   - Engineering implications or considerations
7. If the provided context (including History and Summary) is insufficient, explicitly state that the information is not available.

[Engineering Context]
%s

[User Question]
%s

[Answer Format]
- Use Korean as the primary language.
- Include English technical terms in parentheses on first mention.
- Write in a professional engineering tone.
""";
    }
}
