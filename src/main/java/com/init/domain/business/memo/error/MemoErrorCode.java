package com.init.domain.business.memo.error;

import com.init.global.exception.payload.BaseErrorCode;
import com.init.global.exception.payload.CausedBy;
import com.init.global.exception.payload.ReasonCode;
import com.init.global.exception.payload.StatusCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemoErrorCode implements BaseErrorCode {
    /* 404 NOT FOUND */
    NOT_FOUND(StatusCode.NOT_FOUND, ReasonCode.REQUESTED_RESOURCE_NOT_FOUND, "메모를 찾을 수 없습니다.");

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
