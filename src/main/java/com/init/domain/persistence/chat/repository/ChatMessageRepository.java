package com.init.domain.persistence.chat.repository;

import com.init.domain.persistence.chat.entity.ChatMessage;
import com.init.domain.persistence.chat.entity.ChatRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>,CustomChatRepository {
    @org.springframework.data.jpa.repository.Query("SELECT m FROM ChatMessage m WHERE m.userHisId = :userHisId ORDER BY m.createdAt DESC")
    List<ChatMessage> findRecentMessages(@Param("userHisId") Long userHisId,
                                         Pageable pageable);

    List<ChatMessage> findTop10ByUserHisIdOrderByCreatedAtDesc(Long userHisId);
    List<ChatMessage> findAllByUserHisIdAndChatRoleOrderByCreatedAtAsc(Long userHisId, ChatRole chatRole);
    List<ChatMessage> findAllByUserHisIdOrderByCreatedAtAsc(Long userHisId);
    long countByUserHisId(Long userHisId);
    List<ChatMessage> findTop4ByUserHisIdAndChatRoleOrderByCreatedAtDesc(Long userHisId, ChatRole chatRole);
    List<ChatMessage> findTop7ByUserHisIdAndChatRoleOrderByCreatedAtDesc(Long userHisId, ChatRole chatRole);
}
