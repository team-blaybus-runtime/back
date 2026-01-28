package com.init.infra.oauth.dto;

public record OidcDecodePayload(
        /**
         * Issuer
         * 이 jwt 토큰을 발급한 곳
         * ex) https://kauth.kakao.com
         */
        String iss,
        /**
         * Audience
         * 이 jwt 토큰을 발급받는 사람
         * ex) 앱의 client id
         */
        String aud,
        /**
         * Subject
         * 이 jwt 토큰의 주인
         * ex) 유저의 고유 번호
         */
        String sub,
        String email
) {
}
