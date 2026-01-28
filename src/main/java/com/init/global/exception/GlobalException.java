package com.init.global.exception;

import com.init.global.exception.payload.BaseErrorCode;
import com.init.global.exception.payload.CausedBy;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {
    private final BaseErrorCode errorCode;

    public GlobalException(BaseErrorCode errorCode) {
        super(errorCode.causedBy().reasonCode().name());
        this.errorCode = errorCode;
    }

    public CausedBy causedBy() {
        return errorCode.causedBy();
    }

    public String getExplainError() {
        return errorCode.getExplainError();
    }
}
