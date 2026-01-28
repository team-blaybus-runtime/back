package com.init.infra.security.authorityvalidator;

import com.init.domain.business.user.error.UserErrorCode;
import com.init.global.exception.GlobalException;
import com.init.infra.security.authentication.SecurityUserDetails;
import org.springframework.security.core.Authentication;

public abstract class AuthorityValidator {
    protected SecurityUserDetails isAuthenticated(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new GlobalException(UserErrorCode.FORBIDDEN);
        }

        return (SecurityUserDetails) authentication.getPrincipal();
    }
}
