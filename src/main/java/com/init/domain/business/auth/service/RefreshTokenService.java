package com.init.domain.business.auth.service;

import com.init.domain.cache.refresh.entity.RefreshToken;
import com.init.domain.cache.refresh.repository.RefreshTokenRepository;
import com.init.global.exception.GlobalException;
import com.init.infra.security.error.JwtErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken refresh(Long userId, String oldRefreshToken, String newRefreshToken) {
        String id = RefreshToken.createId(userId);
        RefreshToken savedRefreshToken = refreshTokenRepository.findById(id)
                .orElseThrow(() -> new GlobalException(JwtErrorCode.EXPIRED_TOKEN));

        validateToken(oldRefreshToken, savedRefreshToken);

        savedRefreshToken.rotation(newRefreshToken);
        refreshTokenRepository.save(savedRefreshToken);
        return savedRefreshToken;
    }


    /**
     * @param requestRefreshToken String : 사용자가 보낸 refresh token
     * @param savedRefreshToken   String : Redis에 저장된 refresh token
     * @throws IllegalStateException : 요청한 토큰과 저장된 토큰이 다르다면 토큰이 탈취되었다고 판단하여 값 삭제
     */
    private void validateToken(String requestRefreshToken, RefreshToken savedRefreshToken) throws IllegalStateException {
        if (isTakenAway(requestRefreshToken, savedRefreshToken.getToken())) {
            log.warn("리프레시 토큰 불일치(탈취). expected : {}, actual : {}", requestRefreshToken, savedRefreshToken.getToken());
            refreshTokenRepository.delete(
                    RefreshToken.createId(savedRefreshToken.getUserId())
            );
            log.info("사용자 {}의 리프레시 토큰 삭제", savedRefreshToken.getUserId());

            throw new GlobalException(JwtErrorCode.TAKEN_AWAY_TOKEN);
        }
    }

    /**
     * 토큰 탈취 여부 확인
     *
     * @param requestRefreshToken  String : 사용자가 보낸 refresh token
     * @param expectedRefreshToken String : Redis에 저장된 refresh token
     * @return boolean : 탈취되었다면 true, 아니면 false
     */
    private boolean isTakenAway(String requestRefreshToken, String expectedRefreshToken) {
        return !requestRefreshToken.equals(expectedRefreshToken);
    }

    public void delete(Long userId) {
        refreshTokenRepository.delete(RefreshToken.createId(userId));
    }
}
