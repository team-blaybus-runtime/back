package com.init.domain.persistence.chat.entity;

import com.init.domain.persistence.common.model.DateAuditable;
import com.init.domain.persistence.userstudyhis.entity.UserStudyHis;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Table(name = "chat_summary")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatSummary extends DateAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_summary_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private UserStudyHis userStudyHis;

    @Setter
    @Column(columnDefinition = "text")
    private String summary;

    @Setter
    @Column(columnDefinition = "vector(1536)")
    private float[] summaryEmbedding;

    @Builder
    public ChatSummary(UserStudyHis userStudyHis, String summary, float[] summaryEmbedding) {
        this.userStudyHis = userStudyHis;
        this.summary = summary;
        this.summaryEmbedding = summaryEmbedding;
    }
}
