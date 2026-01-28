package com.init.global.constant;

import lombok.Getter;

@Getter
public enum CacheType {
    SECURITY_USER("securityUser", 60),
    KAKAO_JWKS("kakaoJwks", 3 * 24 * 60 * 60),
    GOOGLE_JWKS("googleJwks", 3 * 24 * 60 * 60);

    private final String cacheName;
    private final long expireAfterWriteSec;
    private final long maximumSize;

    CacheType(String cacheName, long expireAfterWriteSec) {
        this.cacheName = cacheName;
        this.expireAfterWriteSec = expireAfterWriteSec;
        this.maximumSize = CacheConfig.DEFAULT_MAXIMUM_SIZE;
    }

    CacheType(String cacheName, long expireAfterWriteSec, int maximumSize) {
        this.cacheName = cacheName;
        this.expireAfterWriteSec = expireAfterWriteSec;
        this.maximumSize = maximumSize;
    }

    static class CacheConfig {
        static final int DEFAULT_MAXIMUM_SIZE = 1000;
    }
}
