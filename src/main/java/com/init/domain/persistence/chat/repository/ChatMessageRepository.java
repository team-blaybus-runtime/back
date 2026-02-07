package com.init.domain.persistence.chat.repository;

import com.init.domain.persistence.chat.entity.ChatMessage;
import com.init.domain.persistence.chat.entity.ChatRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop10ByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);
    List<ChatMessage> findAllByChatRoomIdAndChatRoleOrderByCreatedAtAsc(Long chatRoomId, ChatRole chatRole);
    List<ChatMessage> findAllByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);
    long countByChatRoomId(Long chatRoomId);
}
