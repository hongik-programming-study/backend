package com.importH.global.error.exception;

import com.importH.global.error.code.ErrorCode;
import lombok.Getter;

@Getter
public class NotificationException extends RuntimeException {
    ErrorCode errorCode;
    String errorMessage;

    public NotificationException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
