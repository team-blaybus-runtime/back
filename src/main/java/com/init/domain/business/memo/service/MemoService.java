package com.init.domain.business.memo.service;

import com.init.application.dto.usermemo.req.MemoCreateReq;
import com.init.application.dto.usermemo.req.MemoUpdateReq;
import com.init.application.dto.usermemo.res.MemoReadRes;
import com.init.domain.business.common.service.EntitySimpReadService;
import com.init.domain.business.user.error.UserErrorCode;
import com.init.domain.persistence.engineering.entity.ProductType;
import com.init.domain.persistence.user.entity.User;
import com.init.domain.persistence.memo.entity.Memo;
import com.init.domain.persistence.memo.repository.MemoRepository;
import com.init.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class MemoService {
    private final MemoRepository memoRepository;
    private final EntitySimpReadService entitySimpReadService;

    @Transactional
    public void create(Long userId, MemoCreateReq req) {
        User user = entitySimpReadService.findUser(userId);
        memoRepository.save(
                Memo.of(user, req.productType(), req.title(), req.content())
        );
    }

    @Transactional(readOnly = true)
    public List<MemoReadRes> read(Long userId, ProductType productType) {
        List<Memo> memos = Objects.isNull(productType) ?
                memoRepository.findByUserId(userId) :
                memoRepository.findByUserIdAndProductType(userId, productType);
        return memos.stream()
                .map(memo -> new MemoReadRes(
                        memo.getId(),
                        memo.getProductType().getDescription(),
                        memo.getTitle(),
                        memo.getContent(),
                        memo.getUpdatedAt()
                ))
                .toList();
    }

    @Transactional
    public void update(Long userId, Long memoId, MemoUpdateReq req) {
        User user = entitySimpReadService.findUser(userId);
        Memo memo = entitySimpReadService.findUserMemo(memoId);

        validateMemoOwner(user, memo);
        memo.update(req.title(), req.content());
    }

    @Transactional
    public void delete(Long userId, Long memoId) {
        User user = entitySimpReadService.findUser(userId);
        Memo memo = entitySimpReadService.findUserMemo(memoId);

        validateMemoOwner(user, memo);
        memoRepository.delete(memo);
    }

    private void validateMemoOwner(User user, Memo memo) {
        if (!memo.getUser().getId().equals(user.getId())) {
            throw new GlobalException(UserErrorCode.FORBIDDEN);
        }
    }
}
