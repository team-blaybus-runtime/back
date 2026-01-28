package com.init.domain.persistence.user.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN"),
    GUEST("ROLE_GUEST"),
    USER("ROLE_USER");

    private final String type;

    public static Role fromType(String type) {
        for (Role role : values()) {
            if (role.type.equals(type)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No enum constant for type: " + type);
    }

    @JsonCreator
    public Role fromString(String type) {
        return valueOf(type.toUpperCase());
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
