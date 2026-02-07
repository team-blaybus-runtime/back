package com.init.application.controller.api;

import com.init.application.dto.userstudyhis.req.UserStudyHisCreateReq;
import com.init.application.dto.userstudyhis.res.UserStudyHisReadRes;
import com.init.infra.security.authentication.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "[사용자 학습 이력 API]")
public interface UserStudyHisApi {
    @Operation(summary = "사용자 학습 이력 생성")
    void create(SecurityUserDetails userDetails,
                UserStudyHisCreateReq req);

    @Operation(summary = "사용자 학습 이력 조회")
    List<UserStudyHisReadRes> read(SecurityUserDetails userDetails);
}
