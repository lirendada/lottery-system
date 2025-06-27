package com.liren.lottery_system.common.enums;

import com.liren.lottery_system.model.ErrorCode;
import lombok.Getter;
import lombok.Setter;

public enum GlobalStatusEnum {
    SUCCESS(200, "成功！"),

    INTERNAL_SERVER_ERROR(500, "系统异常！"),

    UNKNOWN(999, "未知错误");



    @Getter
    @Setter
    ErrorCode errorCode;

    GlobalStatusEnum(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    GlobalStatusEnum(Integer code, String msg) {
        this.errorCode.setCode(code);
        this.errorCode.setErrMsg(msg);
    }
}
