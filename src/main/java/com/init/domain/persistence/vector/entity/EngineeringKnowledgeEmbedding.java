package com.init.domain.persistence.vector.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Data
@Table(name = "engineering_knowledge_embedding")
@AllArgsConstructor
@NoArgsConstructor
public class EngineeringKnowledgeEmbedding {

    @Id
    @Comment("EngineeringKnowledge ID")
    private Long knowledgeId;

    @Column(columnDefinition = "vector(1536)")
    @Comment("임베딩 벡터 (1536차원)")
    private float[] embedding;

    @Comment("제품 간단 분류")
    private String productType;
}
