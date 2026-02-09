package com.init.application.controller.api;

import com.init.application.dto.workflow.req.WorkFlowCreateOrUpdateReq;
import com.init.application.dto.workflow.res.WorkFlowReadRes;
import com.init.infra.security.authentication.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "[워크플로우 API]")
public interface WorkFlowApi {
    @Operation(summary = "워크플로우 생성 API")
    void create(SecurityUserDetails userDetails,
                WorkFlowCreateOrUpdateReq req);

    @Operation(summary = "나의 워크플로우 전체 조회 API")
    List<WorkFlowReadRes> readAll(SecurityUserDetails userDetails);

    @Operation(summary = "워크플로우 수정 API")
    void update(SecurityUserDetails userDetails,
                Long workflowId,
                WorkFlowCreateOrUpdateReq req);

    @Operation(summary = "워크플로우 삭제 API")
    void delete(SecurityUserDetails userDetails,
                Long workflowId);
}
