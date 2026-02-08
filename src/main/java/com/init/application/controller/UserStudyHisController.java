package com.init.application.controller;

import com.init.application.controller.api.UserStudyHisApi;
import com.init.application.dto.userstudyhis.req.UserStudyHisCreateOrUpdateReq;
import com.init.application.dto.userstudyhis.res.UserStudyHisReadRes;
import com.init.domain.business.userstudyhis.service.UserStudyHisService;
import com.init.infra.security.authentication.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserStudyHisController implements UserStudyHisApi {
    private final UserStudyHisService userStudyHisService;

    @Override
    @PostMapping("/user-study-histories")
    public void createOrUpdate(@AuthenticationPrincipal SecurityUserDetails userDetails,
                               @RequestBody @Validated UserStudyHisCreateOrUpdateReq req) {
        userStudyHisService.createOrUpdate(userDetails.getUserId(), req);
    }

    @Override
    @GetMapping("/user-study-histories")
    public List<UserStudyHisReadRes> read(@AuthenticationPrincipal SecurityUserDetails userDetails) {
        return userStudyHisService.read(userDetails.getUserId());
    }
}
