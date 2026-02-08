package com.init.domain.persistence.chat.repository;

import com.init.domain.persistence.chat.entity.ChatSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatSummaryRepository extends JpaRepository<ChatSummary, Long> {
    Optional<ChatSummary> findByUserStudyHisId(Long userHisId);
}
