package com.init.infra.security.jwt;

import java.util.Map;

public interface JwtClaims {
    Map<String, ?> getClaims();
}
