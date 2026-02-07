package com.init.domain.business.common.service;

import com.init.domain.business.memo.error.MemoErrorCode;
import com.init.domain.business.user.error.UserErrorCode;
import com.init.domain.persistence.memo.entity.Memo;
import com.init.domain.persistence.memo.repository.MemoRepository;
import com.init.domain.persistence.user.entity.User;
import com.init.domain.persistence.user.repository.UserRepository;
import com.init.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 엔티티의 PK로 단순 조회하는 기능을 제공하는 서비스입니다.
 *
 * <p>해당 서비스는 다음을 개선합니다:</p>
 * <ol>
 *   <li>타 도메인의 Repository를 통해 엔티티를 조회할 때 발생하는 중복된 예외 처리 제거</li>
 *   <li>타 도메인의 Service를 통해 엔티티를 조회할 때 발생할 수 있는 순환 참조(의존성) 문제 방지</li>
 * </ol>
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class EntitySimpReadService {
    private final UserRepository userRepository;
    private final MemoRepository memoRepository;

    public User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(UserErrorCode.NOT_FOUND));
    }

    public Memo findUserMemo(Long memoId) {
        return memoRepository.findById(memoId)
                .orElseThrow(() -> new GlobalException(MemoErrorCode.NOT_FOUND));
    }
}
