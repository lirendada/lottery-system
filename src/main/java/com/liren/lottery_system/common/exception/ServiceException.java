package com.liren.lottery_system.common.exception;

import com.liren.lottery_system.model.ErrorCode;

public class ServiceException extends RuntimeException {
    private Integer code;
    private String msg;

    ServiceException(ErrorCode errorCode) {
        code = errorCode.getCode();
        msg = errorCode.getErrMsg();
    }
}
