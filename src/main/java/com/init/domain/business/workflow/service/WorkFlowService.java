package com.init.domain.business.workflow.service;

import com.init.application.dto.workflow.req.WorkFlowCreateOrUpdateReq;
import com.init.application.dto.workflow.res.WorkFlowReadRes;
import com.init.domain.business.common.service.EntitySimpReadService;
import com.init.domain.business.user.error.UserErrorCode;
import com.init.domain.persistence.user.entity.User;
import com.init.domain.persistence.worflow.entity.WorkFlow;
import com.init.domain.persistence.worflow.repository.WorkFlowRepository;
import com.init.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WorkFlowService {
    private final WorkFlowRepository workFlowRepository;
    private final EntitySimpReadService entitySimpReadService;

    @Transactional
    public void create(Long userId, WorkFlowCreateOrUpdateReq req) {
        User user = entitySimpReadService.findUser(userId);
        workFlowRepository.save(
                WorkFlow.of(user, req.title(), req.nodeInfo())
        );
    }

    @Transactional
    public List<WorkFlowReadRes> readAll(Long userId) {
        User user = entitySimpReadService.findUser(userId);
        List<WorkFlow> workflows = workFlowRepository.findAllByUser(user);
        return workflows.stream()
                .map(workflow -> new WorkFlowReadRes(
                        workflow.getId(),
                        workflow.getTitle(),
                        workflow.getNodeInfo()
                ))
                .toList();
    }

    @Transactional
    public void update(Long userId, Long workFlowId, WorkFlowCreateOrUpdateReq req) {
        WorkFlow workflow = entitySimpReadService.findWorkFlow(workFlowId);

        if (!workflow.getUser().getId().equals(userId)) {
            throw new GlobalException(UserErrorCode.FORBIDDEN);
        }

        workflow.update(req.title(), req.nodeInfo());
    }

    @Transactional
    public void delete(Long userId, Long workFlowId) {
        WorkFlow workflow = entitySimpReadService.findWorkFlow(workFlowId);

        if (!workflow.getUser().getId().equals(userId)) {
            throw new GlobalException(UserErrorCode.FORBIDDEN);
        }

        workFlowRepository.delete(workflow);
    }
}
