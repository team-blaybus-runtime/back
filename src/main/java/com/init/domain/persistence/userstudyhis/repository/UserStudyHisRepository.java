package com.init.domain.persistence.userstudyhis.repository;

import com.init.domain.persistence.user.entity.User;
import com.init.domain.persistence.userstudyhis.entity.UserStudyHis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStudyHisRepository extends JpaRepository<UserStudyHis, Long> {
    List<UserStudyHis> findByUser(User user);
}
