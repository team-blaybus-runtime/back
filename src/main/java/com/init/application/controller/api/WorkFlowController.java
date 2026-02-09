package com.init.application.controller.api;

import com.init.application.dto.workflow.req.WorkFlowCreateOrUpdateReq;
import com.init.application.dto.workflow.res.WorkFlowReadRes;
import com.init.domain.business.workflow.service.WorkFlowService;
import com.init.infra.security.authentication.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class WorkFlowController implements WorkFlowApi {
    private final WorkFlowService workFlowService;

    @PostMapping("/workflows")
    @Override
    public void create(@AuthenticationPrincipal SecurityUserDetails userDetails,
                       @RequestBody @Validated WorkFlowCreateOrUpdateReq req) {
        workFlowService.create(userDetails.getUserId(), req);
    }

    @GetMapping("/workflows")
    @Override
    public List<WorkFlowReadRes> readAll(@AuthenticationPrincipal SecurityUserDetails userDetails) {
        return workFlowService.readAll(userDetails.getUserId());
    }

    @PutMapping("/workflows/{workflowId}")
    @Override
    public void update(@AuthenticationPrincipal SecurityUserDetails userDetails,
                       @PathVariable Long workflowId,
                       @RequestBody @Validated WorkFlowCreateOrUpdateReq req) {
        workFlowService.update(userDetails.getUserId(), workflowId, req);
    }

    @DeleteMapping("/workflows/{workflowId}")
    @Override
    public void delete(@AuthenticationPrincipal SecurityUserDetails userDetails,
                       @PathVariable Long workflowId) {
        workFlowService.delete(userDetails.getUserId(), workflowId);
    }
}
