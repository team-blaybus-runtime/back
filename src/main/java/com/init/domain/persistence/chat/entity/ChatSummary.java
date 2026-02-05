package com.init.domain.persistence.chat.entity;

import com.init.domain.persistence.common.model.DateAuditable;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatSummary extends DateAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_summary_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false, unique = true)
    private ChatRoom chatRoom;

    @Setter
    @Column(columnDefinition = "text")
    private String summary;

    // 선택: 요약 임베딩 (고급)
    @Setter
    @Column(columnDefinition = "vector(1536)")
    private float[] summaryEmbedding;

    @Builder
    public ChatSummary(ChatRoom chatRoom, String summary, float[] summaryEmbedding) {
        this.chatRoom = chatRoom;
        this.summary = summary;
        this.summaryEmbedding = summaryEmbedding;
    }
}