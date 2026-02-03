package com.init.domain.persistence.EngineeringKnowledge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.errorprone.checker.units.qual.C;
import org.hibernate.annotations.Comment;

@Entity
@Data
@Table(name = "engineering_knowledge")
@AllArgsConstructor
@NoArgsConstructor
public class EngineeringKnowledge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("PK")
    private Long id;

    @Column(columnDefinition = "text")
    @Comment("엔지니어링 지식 원문 텍스트")
    private String content;

    @Column(columnDefinition = "vector(1536)")
    @Comment("임베딩 벡터 (1536차원)")
    private float[] embedding;

    @Comment("제품 간단 분류")
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Comment("제품 카테고리(예: Consumer Quadcopter)")
    private String category;

    @Comment("장비 이름")
    private String equipment;

    @Comment("시스템 구분")
    private String system;

    @Comment("부품 이름")
    private String partName;

    @Comment("재질 정보")
    private String material;
}
