package com.init.global.helper;

import com.init.domain.business.auth.service.RefreshTokenService;
import com.init.domain.cache.refresh.entity.RefreshToken;
import com.init.domain.persistence.user.entity.Role;
import com.init.domain.persistence.user.entity.User;
import com.init.global.annotation.Helper;
import com.init.global.util.JwtClaimsParserUtil;
import com.init.infra.security.jwt.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Helper
public class JwtHelper {
    private final RefreshTokenService refreshTokenService;
    @Qualifier("accessTokenProvider")
    private final JwtProvider accessTokenProvider;
    @Qualifier("refreshTokenProvider")
    private final JwtProvider refreshTokenProvider;

    /**
     * 사용자 정보 기반으로 access token과 refresh token을 생성하는 메서드 <br/>
     *
     * @return {@link Jwts}
     */
    public Jwts createToken(User user) {
        String accessToken = accessTokenProvider.generateToken(AccessTokenClaim.of(user.getId(), user.getRole().getType()));
        String refreshToken = refreshTokenProvider.generateToken(RefreshTokenClaim.of(user.getId(), user.getRole().getType()));

        refreshTokenService.save(RefreshToken.of(user.getId(), refreshToken, toSeconds(refreshTokenProvider.getExpiryDate(refreshToken))));

        return Jwts.of(accessToken, refreshToken);
    }

    /**
     * 빠른 개발을 위해 redis와 같은 Third Party를 적용하지 않아서
     * Refrsh Token을 저장하지 않았으므로 검증 로직 없이 바로 토큰을 재발급합니다.
     */
    public Triple<Long, Role, Jwts> refresh(String refreshToken) {
        JwtClaims claims = refreshTokenProvider.getJwtClaimsFromToken(refreshToken);

        Long userId = JwtClaimsParserUtil.getClaimsValue(claims, RefreshTokenClaimKeys.USER_ID.getValue(), Long::parseLong);
        String roleType = JwtClaimsParserUtil.getClaimsValue(claims, RefreshTokenClaimKeys.ROLE.getValue(), String.class);
        log.debug("refresh token userId : {}, roleType : {}", userId, roleType);


        RefreshToken newRefreshToken = refreshTokenService.refresh(
                userId, refreshToken, refreshTokenProvider.generateToken(RefreshTokenClaim.of(userId, roleType))
        );
        log.debug("new refresh token : {}", newRefreshToken.getToken());

        String newAccessToken = accessTokenProvider.generateToken(AccessTokenClaim.of(userId, roleType));
        log.debug("new access token : {}", newAccessToken);
        return Triple.of(userId, Role.fromType(roleType), Jwts.of(newAccessToken, newRefreshToken.getToken()));
    }

    private long toSeconds(LocalDateTime expiryTime) {
        return Duration.between(LocalDateTime.now(), expiryTime).getSeconds();
    }
}
