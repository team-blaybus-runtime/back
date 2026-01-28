package com.init.infra.security.jwt;

public enum AccessTokenClaimKeys {
    USER_ID("id"),
    ROLE("roleType");

    private final String value;

    AccessTokenClaimKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
