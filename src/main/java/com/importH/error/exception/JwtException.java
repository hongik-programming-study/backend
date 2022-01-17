package com.importH.error.exception;

import com.importH.error.code.ErrorCode;
import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {
    ErrorCode errorCode;
    String errorMessage;

    public JwtException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
