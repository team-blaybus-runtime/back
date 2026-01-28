package com.init.domain.cache.refresh.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString(of = {"userId", "token", "ttl"})
@EqualsAndHashCode(of = {"userId", "token"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    private Long userId;
    private long ttl;
    private String token;
    private LocalDateTime createdAt;

    private static final String ID_FORMAT = "USER::%d::REFRESH_TOKEN";

    public static RefreshToken of(Long userId, String token, long ttl) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.userId = userId;
        refreshToken.token = token;
        refreshToken.ttl = ttl;
        refreshToken.createdAt = LocalDateTime.now();
        return refreshToken;
    }

    public static String createId(Long userId) {
        return ID_FORMAT.formatted(userId);
    }

    public String getId() {
        return ID_FORMAT.formatted(userId);
    }

    public void rotation(String token) {
        this.token = token;
    }
}
