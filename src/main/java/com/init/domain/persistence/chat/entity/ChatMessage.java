package com.init.domain.persistence.chat.entity;

import com.init.domain.persistence.common.model.DateAuditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "chat_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatMessage extends DateAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @Column(columnDefinition = "text")
    private String content;

    @Enumerated(EnumType.STRING)
    private ChatRole chatRole;

    private Long userHisId;

    @Builder
    public ChatMessage(String content, ChatRole chatRole, Long userHisId) {
        this.content = content;
        this.chatRole = chatRole;
        this.userHisId = userHisId;
    }
}
