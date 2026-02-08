package com.init.domain.business.auth.service;

import com.init.application.dto.auth.req.AuthRefreshReq;
import com.init.application.dto.auth.req.AuthSignInReq;
import com.init.application.dto.auth.req.AuthSignUpReq;
import com.init.application.dto.auth.req.AuthUpdatePasswordReq;
import com.init.domain.business.auth.error.AuthErrorCode;
import com.init.domain.business.common.service.EntitySimpReadService;
import com.init.domain.business.user.service.UserService;
import com.init.domain.persistence.user.entity.Role;
import com.init.domain.persistence.user.entity.User;
import com.init.global.exception.GlobalException;
import com.init.global.helper.JwtHelper;
import com.init.infra.security.jwt.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final EntitySimpReadService entitySimpReadService;
    private final UserService userService;
    private final JwtHelper jwtHelper;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Triple<Long, Role, Jwts> signUp(AuthSignUpReq req) {
        if (!isSame(req.password(), req.confirmPassword()))
            throw new GlobalException(AuthErrorCode.PASSWORD_CONFIRM_MISMATCH);

        User user = userService.saveUserWithEncryptedPassword(
                req.email(), bCryptPasswordEncoder.encode(req.password())
        );

        return Triple.of(user.getId(), user.getRole(), jwtHelper.createToken(user));
    }

    private boolean isSame(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    @Transactional(readOnly = true)
    public Triple<Long, Role, Jwts> signIn(AuthSignInReq req) {
        User user = userService.readUserByUsername(req.email());

        validatePassword(req.password(), user.getPassword());

        return Triple.of(user.getId(), user.getRole(), jwtHelper.createToken(user));
    }

    public Triple<Long, Role, Jwts> refresh(AuthRefreshReq req) {
        return jwtHelper.refresh(req.refreshToken());
    }

    @Transactional
    public void updatePassword(Long userId, AuthUpdatePasswordReq req) {
        User user = entitySimpReadService.findUser(userId);

        validatePassword(req.oldPassword(), user.getPassword());

        String newPassword = bCryptPasswordEncoder.encode(req.newPassword());
        user.updatePassword(newPassword);
    }

    private void validatePassword(String requestPassword, String encryptedUserPassword) {
        if (!bCryptPasswordEncoder.matches(requestPassword, encryptedUserPassword)) {
            throw new GlobalException(AuthErrorCode.INVALID_PASSWORD);
        }
    }
}
