package com.init.domain.business.chat.service;

import com.init.domain.persistence.engineering.entity.EngineeringKnowledge;
import com.init.domain.persistence.engineering.entity.ProductType;
import com.init.domain.persistence.engineering.repository.EngineeringKnowledgeRepository;
import com.init.domain.persistence.vector.entity.EngineeringKnowledgeEmbedding;
import com.init.domain.persistence.vector.repository.EngineeringKnowledgeEmbeddingRepository;
import com.init.global.annotation.Helper;
import com.init.infra.openai.client.OpenAiClient;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 엔지니어링 관련 지식 검색을 담당하는 서비스 클래스.
 */
@Helper
@RequiredArgsConstructor
public class EngineeringKnowledgeRetriever {

    private final OpenAiClient openAiClient;
    private final EngineeringKnowledgeRepository engineeringKnowledgeRepo;
    private final EngineeringKnowledgeEmbeddingRepository engineeringKnowledgeEmbeddingRepo;

    /**
     * 주어진 질문과 제품 유형에 따라 관련 엔지니어링 지식을 검색
     * 벡터 임베딩을 사용해 유사성을 평가하고, 데이터베이스에서 메타데이터를 조회
     *
     * @param {string} question 검색에 사용될 질문
     * @param {ProductType} productType 검색 대상이 되는 제품 유형
     * @return {List<EngineeringKnowledge>} 검색된 엔지니어링 지식 리스트. 관련 데이터가 없으면 빈 리스트 반환
     */
    public List<EngineeringKnowledge> retrieve(String question, ProductType productType) {
        float[] embed = openAiClient.embed(question);

        // 1. 벡터 데이터베이스(Postgres)에서 유사한 지식의 ID 검색
        List<EngineeringKnowledgeEmbedding> embeddings = engineeringKnowledgeEmbeddingRepo.searchSimilar(embed, productType.name());

        List<Long> ids = embeddings.stream()
                .map(EngineeringKnowledgeEmbedding::getKnowledgeId)
                .toList();

        // 1.1. 만약 데이터가 존재하지 않은 경우 모든 데이터를 기반으로 ID 생성
        if (ids.isEmpty()) {
            ids = engineeringKnowledgeEmbeddingRepo.findByProductType(productType.name()).stream()
                    .map(EngineeringKnowledgeEmbedding::getKnowledgeId)
                    .toList();
        }

        // 2. 검색된 ID를 바탕으로 메타데이터 데이터베이스(MySQL)에서 상세 정보 조회
        List<EngineeringKnowledge> knowledges = engineeringKnowledgeRepo.findAllById(ids);

        // 정렬 순서 유지 (Postgres에서 넘어온 유사도 순서대로)
        return sortKnowledgesByIds(knowledges, ids);
    }

    /**
     * 입력된 ID 리스트 순서에 따라 엔지니어링 지식을 정렬
     *
     * @param knowledges 데이터베이스에서 조회된 엔지니어링 지식 리스트
     * @param ids 정렬 순서를 지정하는 ID 리스트
     * @return 정렬된 엔지니어링 지식 리스트. ID 리스트와 매핑되지 않는 경우 해당 엔트리는 제외
     */
    private List<EngineeringKnowledge> sortKnowledgesByIds(List<EngineeringKnowledge> knowledges, List<Long> ids) {
        return ids.stream()
                .map(id -> knowledges.stream()
                        .filter(k -> k.getId().equals(id))
                        .findFirst()
                        .orElse(null))
                .filter(java.util.Objects::nonNull)
                .toList();
    }
}
