package com.init.domain.cache.refresh.repository;

import com.init.domain.cache.refresh.entity.RefreshToken;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RefreshTokenRepository {
    private final Map<String, RefreshToken> store = new ConcurrentHashMap<>();

    public void save(RefreshToken refreshToken) {
        store.put(refreshToken.getId(), refreshToken);
    }

    public Optional<RefreshToken> findById(String id) {
        RefreshToken token = store.get(id);
        if (token == null || isExpired(token)) {
            store.remove(id);
            return Optional.empty();
        }
        return Optional.of(token);
    }

    public void delete(String id) {
        store.remove(id);
    }

    private boolean isExpired(RefreshToken token) {
        long ttl = token.getTtl();
        LocalDateTime expiredAt = token.getCreatedAt().plusSeconds(ttl);
        return LocalDateTime.now().isAfter(expiredAt);
    }
}
