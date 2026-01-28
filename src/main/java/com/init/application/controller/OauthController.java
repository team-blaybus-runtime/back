package com.init.application.controller;

import com.init.application.controller.api.OauthApi;
import com.init.application.dto.user.res.AuthenticatedUserRes;
import com.init.domain.business.oauth.service.OauthService;
import com.init.domain.persistence.oauth.entity.OauthProvider;
import com.init.global.util.AuthenticatedUserResCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OauthController implements OauthApi {
    private final OauthService oauthService;

    @Override
    @PreAuthorize("isAnonymous()")
    @GetMapping("/oauth/sign-in")
    public ResponseEntity<AuthenticatedUserRes> signInOrSignUp(@RequestParam OauthProvider oauthProvider,
                                                               @RequestParam String code) {
        return AuthenticatedUserResCreator.from(oauthService.signInOrSignUp(oauthProvider, code));
    }
}
