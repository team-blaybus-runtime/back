package com.init.domain.persistence.memo.repository;

import com.init.domain.persistence.engineering.entity.ProductType;
import com.init.domain.persistence.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findByUserId(Long userId);

    List<Memo> findByUserIdAndProductType(Long userId, ProductType productType);
}
