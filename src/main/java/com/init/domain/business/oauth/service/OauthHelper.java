package com.init.domain.business.oauth.service;

import com.init.domain.persistence.oauth.entity.OauthProvider;
import com.init.global.annotation.Helper;
import com.init.global.util.OauthRequestBodyUtil;
import com.init.infra.oauth.client.GoogleOidcClient;
import com.init.infra.oauth.client.KakaoOidcClient;
import com.init.infra.oauth.client.OauthOidcClient;
import com.init.infra.oauth.dto.OauthTokenRes;
import com.init.infra.oauth.dto.OidcDecodePayload;
import com.init.infra.oauth.dto.OidcPublicKey;
import com.init.infra.oauth.dto.OidcPublicKeyRes;
import com.init.infra.oauth.properties.GoogleOidcProperties;
import com.init.infra.oauth.properties.KakaoOidcProperties;
import com.init.infra.oauth.properties.OauthOidcClientProperties;
import com.init.infra.oauth.provider.OauthOidcProvider;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Helper
public class OauthHelper {
    private final OauthOidcProvider oauthOidcProvider;
    private final Map<OauthProvider, Map<OauthOidcClient, OauthOidcClientProperties>> oauthOidcClients;

    public OauthHelper(
            OauthOidcProvider oauthOidcProvider,
            KakaoOidcClient kakaoOauthClient,
            GoogleOidcClient googleOauthClient,
            KakaoOidcProperties kakaoOauthClientProperties,
            GoogleOidcProperties googleOauthClientProperties
    ) {
        this.oauthOidcProvider = oauthOidcProvider;
        oauthOidcClients = Map.of(
                OauthProvider.KAKAO, Map.of(kakaoOauthClient, kakaoOauthClientProperties),
                OauthProvider.GOOGLE, Map.of(googleOauthClient, googleOauthClientProperties)
        );
    }

    /**
     * Provider에 따라 Client와 Properties를 선택하고 Odic public key 정보를 가져와서 ID Token의 payload를 추출하는 메서드
     *
     * @param oauthProvider : {@link OauthProvider}
     * @param idToken       : code
     * @return OIDCDecodePayload : ID Token의 payload
     */
    public OidcDecodePayload getOidcDecodedPayload(OauthProvider oauthProvider, String idToken) {
        OauthOidcClient client = oauthOidcClients.get(oauthProvider).keySet().iterator().next();
        OauthOidcClientProperties properties = oauthOidcClients.get(oauthProvider).values().iterator().next();
        OidcPublicKeyRes response = client.getOidcPublicKey();

        return getPayloadFromIdToken(idToken, properties.getIssuer(), properties.getClientId(), properties.getNonce(), response);
    }

    /**
     * ID Token의 payload를 추출하는 메서드 <br/>
     * OAuth 2.0 spec에 따라 ID Token의 유효성 검사 수행 <br/>
     *
     * @param idToken  : code
     * @param iss      : ID Token을 발급한 provider의 URL
     * @param aud      : ID Token이 발급된 앱의 앱 키
     * @param nonce    : 인증 서버 로그인 요청 시 전달한 임의의 문자열 (Optional, 현재는 사용하지 않음)
     * @param response : 공개키 목록
     * @return OIDCDecodePayload : ID Token의 payload
     */
    private OidcDecodePayload getPayloadFromIdToken(String idToken, String iss, String aud, String nonce, OidcPublicKeyRes response) {
        String kid = oauthOidcProvider.getKidFromUnsignedTokenHeader(idToken, iss, aud, nonce);

        OidcPublicKey key = response.getKeys().stream()
                .filter(k -> k.kid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No matching key found"));
        return oauthOidcProvider.getOIDCTokenBody(idToken, key.n(), key.e());
    }

    public OauthTokenRes getIdToken(OauthProvider oauthProvider, String code) {
        OauthOidcClient client = oauthOidcClients.get(oauthProvider).keySet().iterator().next();
        OauthOidcClientProperties properties = oauthOidcClients.get(oauthProvider).values().iterator().next();

        MultiValueMap<String, String> body = OauthRequestBodyUtil.createIdTokenRequestBody(
                code,
                properties.getClientId(),
                properties.getClientSecret(),
                properties.getRedirectUri()
        );

        return client.getIdToken(body);
    }
}
