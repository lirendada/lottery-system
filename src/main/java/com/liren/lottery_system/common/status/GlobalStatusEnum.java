package com.liren.lottery_system.common.status;

import lombok.Getter;
import lombok.Setter;

public enum GlobalStatusEnum {
    SUCCESS(200, "成功"),

    INTERNAL_SERVER_ERROR(500, "系统异常"),

    UNKNOWN(999, "未知错误");



    @Getter
    @Setter
    Integer code;

    @Getter
    @Setter
    String msg;

    GlobalStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
