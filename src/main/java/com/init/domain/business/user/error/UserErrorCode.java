package com.init.domain.business.user.error;

import com.init.global.exception.payload.BaseErrorCode;
import com.init.global.exception.payload.CausedBy;
import com.init.global.exception.payload.ReasonCode;
import com.init.global.exception.payload.StatusCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserErrorCode implements BaseErrorCode {
    /* 403 FORBIDDEN */
    FORBIDDEN(StatusCode.FORBIDDEN, ReasonCode.ACCESS_TO_THE_REQUESTED_RESOURCE_IS_FORBIDDEN, "사용자에게 권한이 없습니다."),

    /* 404 NOT FOUND */
    NOT_FOUND(StatusCode.NOT_FOUND, ReasonCode.REQUESTED_RESOURCE_NOT_FOUND, "회원을 찾을 수 없습니다."),
    CONFLICT_USERNAME(StatusCode.CONFLICT, ReasonCode.RESOURCE_ALREADY_EXISTS, "이미 존재하는 유저 아이디입니다");

    private final StatusCode statusCode;
    private final ReasonCode reasonCode;
    private final String message;

    @Override
    public CausedBy causedBy() {
        return CausedBy.of(statusCode, reasonCode);
    }

    @Override
    public String getExplainError() throws NoSuchFieldError {
        return message;
    }
}
