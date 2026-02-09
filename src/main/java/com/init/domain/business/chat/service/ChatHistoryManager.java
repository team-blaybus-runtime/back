package com.init.domain.business.chat.service;

import com.init.domain.persistence.chat.entity.ChatMessage;
import com.init.domain.persistence.chat.entity.ChatRole;
import com.init.domain.persistence.chat.repository.ChatMessageRepository;
import com.init.domain.persistence.chat.repository.ChatSummaryRepository;
import com.init.domain.persistence.chat.entity.ChatSummary;
import com.init.global.annotation.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 채팅 기록 관리를 담당하는 서비스 클래스.
 */
@Helper
@RequiredArgsConstructor
public class ChatHistoryManager {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatSummaryRepository chatSummaryRepository;

    public void saveMessage(Long userHisId, String content, ChatRole role) {
        chatMessageRepository.save(ChatMessage.builder()
                .userHisId(userHisId)
                .content(content)
                .chatRole(role)
                .build());
    }

    public Optional<String> getSummary(Long userHisId) {
        return chatSummaryRepository.findByUserStudyHisId(userHisId)
                .map(ChatSummary::getSummary);
    }

    public List<ChatMessage> getRecentMessages(Long userHisId) {
        return getRecentMessages(userHisId, 10);
    }

    public List<ChatMessage> getRecentMessages(Long userHisId, int limit) {
        List<ChatMessage> messages = chatMessageRepository.findRecentMessages(userHisId, PageRequest.of(0, limit));
        Collections.reverse(messages);
        return messages;
    }

    public List<ChatMessage> getAllQuestions(Long userHisId) {
        return chatMessageRepository.findAllByUserHisIdAndChatRoleOrderByCreatedAtAsc(userHisId, ChatRole.QUESTION);
    }

    public List<ChatMessage> getRecentQuestionsTop7(Long userHisId) {
        List<ChatMessage> questions = chatMessageRepository.findTop7ByUserHisIdAndChatRoleOrderByCreatedAtDesc(userHisId, ChatRole.QUESTION);
        Collections.reverse(questions);
        return questions;
    }


    public List<ChatMessage> getRecentQuestions(Long userHisId) {
        List<ChatMessage> questions = chatMessageRepository.findTop4ByUserHisIdAndChatRoleOrderByCreatedAtDesc(userHisId, ChatRole.QUESTION);
        Collections.reverse(questions);
        return questions;
    }


    public long getMessageCount(Long userHisId) {
        return chatMessageRepository.countByUserHisId(userHisId);
    }

    public List<ChatMessage> getChatMessagesByUserHisId(Long userHisId) {
        return chatMessageRepository.findAllByUserHisIdOrderByCreatedAtAsc(userHisId);
    }
}
