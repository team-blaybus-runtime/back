package com.init.domain.persistence.vector.repository;

import com.init.domain.persistence.vector.entity.EngineeringKnowledgeEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EngineeringKnowledgeEmbeddingRepository extends JpaRepository<EngineeringKnowledgeEmbedding, Long> {

    @Query(value = """
    SELECT 
        knowledge_id, 
        product_type,
        NULL as embedding 
    FROM engineering_knowledge_embedding 
    WHERE product_type = :productType
    ORDER BY embedding <=> CAST(:embedding AS vector)
    LIMIT 5
    """, nativeQuery = true)
    List<EngineeringKnowledgeEmbedding> searchSimilar(@Param("embedding") float[] embedding, @Param("productType") String productType);
}
