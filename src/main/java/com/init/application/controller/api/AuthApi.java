package com.init.application.controller.api;

import com.init.application.dto.auth.req.AuthRefreshReq;
import com.init.application.dto.auth.req.AuthSignInReq;
import com.init.application.dto.auth.req.AuthSignUpReq;
import com.init.application.dto.auth.req.AuthUpdatePasswordReq;
import com.init.application.dto.user.res.AuthenticatedUserRes;
import com.init.infra.security.authentication.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "[인증 API]")
public interface AuthApi {

    @Operation(summary = "일반 회원가입",
            description = """
                    <li>nickname : 한글, 영문, 숫자만을 사용하여 2~10자로 입력되어야 합니다.</li>
                    <li>username : 영문 소문자, 숫자만을 사용하여 5~15자로 입력되어야 합니다."</li>
                    <li>password : 8~16자의 영문 대소문자, 숫자, 특수문자(@$!%*?&)로 구성해야 하며, 반드시 하나 이상의 영문 소문자와 숫자를 포함해야 합니다.</li>
                    """
    )
    ResponseEntity<AuthenticatedUserRes> signUp(AuthSignUpReq authSignUpReq);

    @Operation(summary = "일반 로그인",
            description = """
                    <li>username : 영문 소문자, 숫자만을 사용하여 5~15자로 입력되어야 합니다."</li>
                    <li>password : 8~16자의 영문 대소문자, 숫자, 특수문자(@$!%*?&)로 구성해야 하며, 반드시 하나 이상의 영문 소문자와 숫자를 포함해야 합니다.</li>
                    """)
    ResponseEntity<AuthenticatedUserRes> signIn(AuthSignInReq authSignInReq);

    @Operation(summary = "토큰 재발급")
    ResponseEntity<AuthenticatedUserRes> refresh(AuthRefreshReq req);

    @Operation(summary = "비밀번호 변경")
    void updatePassword(SecurityUserDetails user,
                        AuthUpdatePasswordReq authUpdatePasswordReq);
}
