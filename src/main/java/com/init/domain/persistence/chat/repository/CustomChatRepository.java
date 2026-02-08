package com.init.domain.persistence.chat.repository;

import com.init.domain.persistence.chat.entity.ChatMessage;
import org.springframework.data.domain.Slice;

public interface CustomChatRepository {

    Slice<ChatMessage> findByUserIdAndHistoryId(Long userId, Long historyId,Long lastId, String order, Integer limit);
}
