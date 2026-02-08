package com.init.domain.persistence.engineering.repository;

import com.init.domain.persistence.engineering.entity.EngineeringKnowledge;
import com.init.domain.persistence.engineering.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EngineeringKnowledgeRepository extends JpaRepository<EngineeringKnowledge, Long> {
    List<EngineeringKnowledge> findByProductType(ProductType productType);
}
