package com.init.application.controller;

import com.init.application.controller.api.MemoApi;
import com.init.application.dto.usermemo.req.MemoCreateReq;
import com.init.application.dto.usermemo.req.MemoUpdateReq;
import com.init.application.dto.usermemo.res.MemoReadRes;
import com.init.domain.business.memo.service.MemoService;
import com.init.domain.persistence.engineering.entity.ProductType;
import com.init.infra.security.authentication.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemoController implements MemoApi {
    private final MemoService memoService;

    @PostMapping("/users/memos")
    public void create(@AuthenticationPrincipal SecurityUserDetails userDetails,
                       @RequestBody MemoCreateReq req) {
        memoService.create(userDetails.getUserId(), req);
    }

    @GetMapping("/users/memos")
    public List<MemoReadRes> read(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                  @RequestParam(required = false) ProductType productType) {
        return memoService.read(userDetails.getUserId(), productType);
    }

    @PutMapping("/users/memos/{memoId}")
    public void update(@AuthenticationPrincipal SecurityUserDetails userDetails,
                       @PathVariable Long memoId,
                       @RequestBody MemoUpdateReq req) {
        memoService.update(userDetails.getUserId(), memoId, req);
    }

    @DeleteMapping("/users/memos/{memoId}")
    public void delete(@AuthenticationPrincipal SecurityUserDetails userDetails,
                       @PathVariable Long memoId) {
        memoService.delete(userDetails.getUserId(), memoId);
    }
}
