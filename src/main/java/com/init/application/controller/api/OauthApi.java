package com.init.application.controller.api;

import com.init.application.dto.user.res.AuthenticatedUserRes;
import com.init.domain.persistence.oauth.entity.OauthProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "[소셜 인증 API]")
public interface OauthApi {
    @Operation(
            summary = "소셜 로그인",
            description = """
                    <p> 유저가 아래 링크로 접속하여 로그인에 성공하면 자동으로 소셜 로그인 API로 Redirect되기 때문에 따로 이 API를 호출할 필요가 없습니다. </p>
                    <p> 만약 가입되지 않은 사용자라면 GUEST로 생성 후 로그인 성공 응답을 반환합니다. </p>
                    <p>
                        <a href=\"https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=fa0131708b8498e20bf515f928d8b1e0&redirect_uri=http://localhost:8080/oauth/sign-in?oauthProvider=kakao&scope=openid%20profile_nickname&nonce=example-nonce\">KAKAO Login</a>
                        : https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=fa0131708b8498e20bf515f928d8b1e0&redirect_uri=http://localhost:8080/oauth/sign-in?oauthProvider=kakao&scope=openid%20profile_nickname&nonce=example-nonce
                    </p>
                    <p>
                        <a href=\"https://accounts.google.com/o/oauth2/v2/auth?client_id=248388975343-0oo0f79rrsqpf1k63ahpivkhd2rfu1jp.apps.googleusercontent.com&redirect_uri=http://localhost:8080/oauth/sign-in?oauthProvider=google&response_type=code&scope=openid%20email%20profile&nonce=example-nonce\">GOOGLE Login</a>
                        : https://accounts.google.com/o/oauth2/v2/auth?client_id=248388975343-0oo0f79rrsqpf1k63ahpivkhd2rfu1jp.apps.googleusercontent.com&redirect_uri=http://localhost:8080/oauth/sign-in?oauthProvider=google&response_type=code&scope=openid%20email%20profile&nonce=example-nonce
                    </p>
                    """
    )
    ResponseEntity<AuthenticatedUserRes> signInOrSignUp(OauthProvider oauthProvider,
                                                        String code);
}
