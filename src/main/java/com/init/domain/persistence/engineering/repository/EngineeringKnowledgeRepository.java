package com.init.domain.persistence.engineering.repository;

import com.init.domain.persistence.engineering.entity.EngineeringKnowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EngineeringKnowledgeRepository extends JpaRepository<EngineeringKnowledge, Long> {
}
