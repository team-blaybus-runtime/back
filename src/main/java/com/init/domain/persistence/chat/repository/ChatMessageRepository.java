package com.init.domain.persistence.chat.repository;

import com.init.domain.persistence.chat.entity.ChatMessage;
import com.init.domain.persistence.chat.entity.ChatRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>,CustomChatRepository {
    List<ChatMessage> findTop10ByUserHisIdOrderByCreatedAtDesc(Long userHisId);
    List<ChatMessage> findAllByUserHisIdAndChatRoleOrderByCreatedAtAsc(Long userHisId, ChatRole chatRole);
    List<ChatMessage> findAllByUserHisIdOrderByCreatedAtAsc(Long userHisId);
    long countByUserHisId(Long userHisId);
}
