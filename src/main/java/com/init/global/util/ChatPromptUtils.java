package com.init.global.util;

import com.init.domain.persistence.chat.entity.ChatMessage;
import com.init.domain.persistence.chat.entity.ChatRole;
import com.init.global.annotation.Util;

import java.util.List;
import java.util.Map;

@Util
public class ChatPromptUtils {
    /**
     * ChatMessage 리스트를 "Q: 내용", "A: 내용" 형식의 문자열로 변환합니다.
     */
    public static String formatChatHistory(List<ChatMessage> messages) {
        if (messages == null || messages.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (ChatMessage m : messages) {
            String roleLabel = m.getChatRole() == ChatRole.QUESTION ? "Q" : "A";
            sb.append(roleLabel).append(": ").append(m.getContent()).append("\n");
        }
        return sb.toString();
    }

    /**
     * 단일 텍스트 내용을 OpenAI API 호출 규격(Map List)으로 변환합니다.
     */
    public static List<Map<String, String>> toUserMessages(String content) {
        return List.of(Map.of("role", "user", "content", content));
    }
}
