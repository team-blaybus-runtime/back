package com.init.application.controller;

import com.init.application.controller.api.AuthApi;
import com.init.application.dto.auth.req.AuthRefreshReq;
import com.init.application.dto.auth.req.AuthSignInReq;
import com.init.application.dto.auth.req.AuthSignUpReq;
import com.init.application.dto.auth.req.AuthUpdatePasswordReq;
import com.init.application.dto.user.res.AuthenticatedUserRes;
import com.init.domain.business.auth.service.AuthService;
import com.init.global.util.AuthenticatedUserResCreator;
import com.init.infra.security.authentication.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;

    @Override
    @PostMapping("/sign-up")
    public ResponseEntity<AuthenticatedUserRes> signUp(@RequestBody @Validated AuthSignUpReq req) {
        return AuthenticatedUserResCreator.from(authService.signUp(req));
    }

    @Override
    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticatedUserRes> signIn(@RequestBody @Validated AuthSignInReq req) {
        return AuthenticatedUserResCreator.from(authService.signIn(req));
    }

    @Override
    @PreAuthorize("isAnonymous()")
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticatedUserRes> refresh(@RequestBody @Validated AuthRefreshReq req) {
        return AuthenticatedUserResCreator.from(authService.refresh(req));
    }

    @Override
    @PutMapping("/password")
    public void updatePassword(@AuthenticationPrincipal SecurityUserDetails user,
                               @RequestBody @Validated AuthUpdatePasswordReq req) {
        authService.updatePassword(user.getUserId(), req);
    }
}
