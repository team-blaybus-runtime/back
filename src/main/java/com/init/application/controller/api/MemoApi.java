package com.init.application.controller.api;

import com.init.application.dto.usermemo.req.MemoCreateReq;
import com.init.application.dto.usermemo.req.MemoUpdateReq;
import com.init.application.dto.usermemo.res.MemoReadRes;
import com.init.domain.persistence.engineering.entity.ProductType;
import com.init.infra.security.authentication.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "[사용자 메모 API]")
public interface MemoApi {
    @Operation(summary = "메모 생성")
    void create(SecurityUserDetails userDetails,
                MemoCreateReq req);

    @Operation(summary = "메모 조회", description = "ProductType을 지정하지 않을 경우 전체 메모를 조회합니다.")
    List<MemoReadRes> read(SecurityUserDetails userDetails,
                           ProductType productType);

    @Operation(summary = "메모 수정")
    void update(SecurityUserDetails userDetails,
                Long memoId,
                MemoUpdateReq req);

    @Operation(summary = "메모 삭제")
    void delete(SecurityUserDetails userDetails,
                Long memoId);
}
