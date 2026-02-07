package com.init.domain.business.chat.service;

import com.init.domain.persistence.chat.entity.ChatMessage;
import com.init.domain.persistence.chat.entity.ChatRole;
import com.init.domain.persistence.chat.entity.ChatRoom;
import com.init.domain.persistence.chat.repository.ChatMessageRepository;
import com.init.domain.persistence.chat.repository.ChatRoomRepository;
import com.init.domain.persistence.chat.repository.ChatSummaryRepository;
import com.init.domain.persistence.chat.entity.ChatSummary;
import com.init.global.annotation.Helper;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 채팅 기록 관리를 담당하는 서비스 클래스.
 */
@Helper
@RequiredArgsConstructor
public class ChatHistoryManager {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatSummaryRepository chatSummaryRepository;

    public ChatRoom getOrCreateChatRoom(Long userId, Long chatRoomId) {
        if (chatRoomId != null) {
            return chatRoomRepository.findById(chatRoomId)
                    .orElseGet(() -> chatRoomRepository.save(ChatRoom.builder().userId(userId).build()));
        }
        return chatRoomRepository.save(ChatRoom.builder().userId(userId).build());
    }

    public void saveMessage(Long chatRoomId, String content, ChatRole role) {
        chatMessageRepository.save(ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .content(content)
                .chatRole(role)
                .build());
    }

    public Optional<String> getSummary(Long chatRoomId) {
        return chatSummaryRepository.findByChatRoomId(chatRoomId)
                .map(ChatSummary::getSummary);
    }

    public List<ChatMessage> getRecentMessages(Long chatRoomId) {
        List<ChatMessage> messages = chatMessageRepository.findTop10ByChatRoomIdOrderByCreatedAtDesc(chatRoomId);
        Collections.reverse(messages);
        return messages;
    }

    public List<ChatMessage> getAllQuestions(Long chatRoomId) {
        return chatMessageRepository.findAllByChatRoomIdAndChatRoleOrderByCreatedAtAsc(chatRoomId, ChatRole.QUESTION);
    }

    public long getMessageCount(Long chatRoomId) {
        return chatMessageRepository.countByChatRoomId(chatRoomId);
    }

    public List<ChatRoom> getChatRoomsByUserId(Long userId) {
        return chatRoomRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<ChatMessage> getChatMessagesByChatRoomId(Long chatRoomId) {
        return chatMessageRepository.findAllByChatRoomIdOrderByCreatedAtAsc(chatRoomId);
    }
}
