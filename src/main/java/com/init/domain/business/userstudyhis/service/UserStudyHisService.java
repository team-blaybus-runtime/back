package com.init.domain.business.userstudyhis.service;

import com.init.application.dto.userstudyhis.req.UserStudyHisCreateOrUpdateReq;
import com.init.application.dto.userstudyhis.res.UserStudyHisReadRes;
import com.init.domain.business.common.service.EntitySimpReadService;
import com.init.domain.business.engineering.service.ProductImageUrlMapper;
import com.init.domain.persistence.engineering.entity.ProductType;
import com.init.domain.persistence.user.entity.User;
import com.init.domain.persistence.userstudyhis.entity.UserStudyHis;
import com.init.domain.persistence.userstudyhis.repository.UserStudyHisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserStudyHisService {
    private final EntitySimpReadService entitySimpReadService;
    private final UserStudyHisRepository userStudyHisRepository;
    private final ProductImageUrlMapper productImageUrlMapper;

    @Transactional
    public void createOrUpdate(Long userId, UserStudyHisCreateOrUpdateReq req) {
        Optional<UserStudyHis> userStudyHis = userStudyHisRepository.findByUserIdAndProductType(userId, req.productType());
        if (userStudyHis.isPresent()) {
            userStudyHis.get().update(req.title(), req.viewInfo());
        } else {
            User user = entitySimpReadService.findUser(userId);
            userStudyHisRepository.save(
                    UserStudyHis.of(user, req.productType(), req.title(), req.viewInfo())
            );
        }
    }

    @Transactional(readOnly = true)
    public List<UserStudyHisReadRes> read(Long userId) {
        User user = entitySimpReadService.findUser(userId);
        List<UserStudyHis> userStudyHisList = userStudyHisRepository.findByUser(user);

        return userStudyHisList.stream()
                .map(userStudyHis -> {
                    ProductType productType = userStudyHis.getProductType();
                    return new UserStudyHisReadRes(
                            userStudyHis.getId(),
                            userStudyHis.getTitle(),
                            userStudyHis.getUpdatedAt(),
                            userStudyHis.getViewInfo(),
                            productType.getDescription(),
                            productImageUrlMapper.getImageUrlForType(productType)
                    );
                })
                .toList();
    }
}
