package com.init.domain.business.oauth.service;

import com.init.domain.business.user.service.UserService;
import com.init.domain.persistence.oauth.entity.Oauth;
import com.init.domain.persistence.oauth.entity.OauthProvider;
import com.init.domain.persistence.oauth.repository.OauthRepository;
import com.init.domain.persistence.user.entity.Role;
import com.init.domain.persistence.user.entity.User;
import com.init.global.helper.JwtHelper;
import com.init.infra.oauth.dto.OauthTokenRes;
import com.init.infra.oauth.dto.OidcDecodePayload;
import com.init.infra.security.jwt.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Service
public class OauthService {
    private final UserService userService;
    private final OauthHelper oauthHelper;
    private final JwtHelper jwtHelper;
    private final OauthRepository oauthRepository;

    @Transactional
    public Triple<Long, Role, Jwts> signInOrSignUp(OauthProvider oauthProvider, String code) {
        code = URLDecoder.decode(code, StandardCharsets.UTF_8);
        OauthTokenRes tokenRes = oauthHelper.getIdToken(oauthProvider, code);
        OidcDecodePayload payload = oauthHelper.getOidcDecodedPayload(oauthProvider, tokenRes.idToken());
        log.debug("payload : {}", payload);

        User user = oauthRepository.findBySubAndOauthProvider(payload.sub(), oauthProvider)
                .map(oauth -> oauth.getUser())
                .orElseGet(() -> saveGuest(oauthProvider, payload));

        return Triple.of(user.getId(), user.getRole(), jwtHelper.createToken(user));
    }

    private User saveGuest(OauthProvider oauthProvider, OidcDecodePayload payload) {
        User newUser = userService.saveGuest();
        oauthRepository.save(Oauth.of(oauthProvider, payload.sub(), newUser));
        return newUser;
    }
}
