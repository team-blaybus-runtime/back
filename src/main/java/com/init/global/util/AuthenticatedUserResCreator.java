package com.init.global.util;

import com.init.application.dto.user.res.AuthenticatedUserRes;
import com.init.domain.persistence.user.entity.Role;
import com.init.global.annotation.Util;
import com.init.infra.security.jwt.AuthConstants;
import com.init.infra.security.jwt.Jwts;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.http.ResponseEntity;

@Util
public final class AuthenticatedUserResCreator {
    public static ResponseEntity<AuthenticatedUserRes> from(Triple<Long, Role, Jwts> userInfo) {
        Long userId = userInfo.getLeft();
        Role role = userInfo.getMiddle();
        Jwts jwts = userInfo.getRight();

        return ResponseEntity.ok()
                .body(
                        new AuthenticatedUserRes(
                                userId,
                                role.getType(),
                                jwts.accessToken(),
                                jwts.refreshToken(),
                                AuthConstants.TOKEN_TYPE.getValue()
                        )
                );
    }
}
