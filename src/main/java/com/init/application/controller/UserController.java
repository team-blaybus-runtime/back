package com.init.application.controller;

import com.init.application.controller.api.UserApi;
import com.init.application.dto.user.req.NicknameCheckReq;
import com.init.application.dto.user.req.NicknameUpdateReq;
import com.init.application.dto.user.req.ProfileUpdateReq;
import com.init.application.dto.user.res.UserDetailRes;
import com.init.domain.business.user.service.UserService;
import com.init.infra.security.authentication.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    @GetMapping("/users/me")
    public UserDetailRes readUserDetail(@AuthenticationPrincipal SecurityUserDetails user) {
        return userService.readUserDetail(user.getUserId());
    }

    @Override
    @GetMapping("/users")
    public List<UserDetailRes> readUserDetails() {
        return userService.readUserDetails();
    }

    @Override
    @GetMapping("/users/username")
    public Map<String, Boolean> isDuplicatedUsername(@RequestParam @Validated String username) {
        return Map.of("isDuplicated", userService.isDuplicatedUsername(username));
    }

    @Override
    @GetMapping("/users/nickname")
    public Map<String, Boolean> isDuplicatedNickname(@Validated NicknameCheckReq req) {
        return Map.of("isDuplicated", userService.isDuplicatedNickname(req));
    }

    @Override
    @PutMapping("/users/nickname")
    public void updatePassword(@AuthenticationPrincipal SecurityUserDetails user,
                               @RequestBody NicknameUpdateReq req) {
        userService.updateNickname(user.getUserId(), req);
    }

    @Override
    @PutMapping("/users/profiles")
    public void updateProfile(@AuthenticationPrincipal SecurityUserDetails user,
                              @RequestBody @Validated ProfileUpdateReq req) {
        userService.updateProfile(user.getUserId(), req);
    }

    @Override
    @DeleteMapping("/users")
    public void delete(@AuthenticationPrincipal SecurityUserDetails user) {
        userService.delete(user.getUserId());
    }
}
