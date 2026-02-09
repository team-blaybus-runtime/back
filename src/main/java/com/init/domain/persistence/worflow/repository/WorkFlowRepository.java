package com.init.domain.persistence.worflow.repository;

import com.init.domain.persistence.user.entity.User;
import com.init.domain.persistence.worflow.entity.WorkFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkFlowRepository extends JpaRepository<WorkFlow, Long> {
    List<WorkFlow> findAllByUser(User user);
}
