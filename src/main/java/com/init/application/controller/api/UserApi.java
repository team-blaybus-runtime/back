package com.init.application.controller.api;

import com.init.application.dto.user.req.NicknameCheckReq;
import com.init.application.dto.user.req.NicknameUpdateReq;
import com.init.application.dto.user.req.ProfileUpdateReq;
import com.init.application.dto.user.res.UserDetailRes;
import com.init.infra.security.authentication.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.Map;

@Tag(name = "[유저 API]")
public interface UserApi {

    @Operation(summary = "내 정보 조회")
    UserDetailRes readUserDetail(SecurityUserDetails user);

    @Operation(summary = "전체 회원 조회")
    List<UserDetailRes> readUserDetails();

    @Operation(summary = "유저 아이디 중복 확인")
    Map<String, Boolean> isDuplicatedUsername(String username);

    @Operation(summary = "닉네임 중복 확인")
    Map<String, Boolean> isDuplicatedNickname(NicknameCheckReq nicknameCheckReq);

    @Operation(summary = "닉네임 변경")
    void updatePassword(SecurityUserDetails user,
                        NicknameUpdateReq nicknameUpdateReq);

    @Operation(summary = "프로필 변경")
    void updateProfile(SecurityUserDetails user,
                       ProfileUpdateReq req);

    @DeleteMapping("/users")
    void delete(@AuthenticationPrincipal SecurityUserDetails user);
}
