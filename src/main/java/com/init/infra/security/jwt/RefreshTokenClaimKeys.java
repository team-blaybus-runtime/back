package com.init.infra.security.jwt;

public enum RefreshTokenClaimKeys {
    USER_ID("id"),
    ROLE("roleType");

    private final String value;

    RefreshTokenClaimKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
