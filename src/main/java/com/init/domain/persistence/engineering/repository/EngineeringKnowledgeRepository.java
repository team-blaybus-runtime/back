package com.init.domain.persistence.engineering.repository;

import com.init.domain.persistence.engineering.entity.EngineeringKnowledge;
import com.init.domain.persistence.engineering.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EngineeringKnowledgeRepository extends JpaRepository<EngineeringKnowledge, Long> {
    List<EngineeringKnowledge> findByProductType(ProductType productType);

    @Query("SELECT new com.init.domain.persistence.engineering.entity.EngineeringKnowledge(" +
            "k.id, k.content, k.productType, k.category, k.equipment, k.systemClassification, k.partName, k.material, k.assetUrl, k.imageUrl, null) " +
            "FROM EngineeringKnowledge k WHERE k.id IN :ids")
    List<EngineeringKnowledge> findAllByIdsWithoutPosition(@Param("ids") List<Long> ids);
}
