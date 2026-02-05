package com.init.domain.persistence.chat.repository;

import com.init.domain.persistence.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop10ByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);
    long countByChatRoomId(Long chatRoomId);
}
