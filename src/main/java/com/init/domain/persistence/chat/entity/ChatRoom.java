package com.init.domain.persistence.chat.entity;

import com.init.domain.persistence.common.model.DateAuditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoom extends DateAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    private Long userId;

    @Builder
    public ChatRoom(Long userId) {
        this.userId = userId;
    }
}
