package com.liren.lottery_system.common.status;

import lombok.Getter;
import lombok.Setter;

public enum ControllerStatusEnum {
    // ------ 人员模块错误码 --------

    REGISTER_ERROR(100, "注册失败"),
    LOGIN_ERROR(101, "登录失败");

    // ------ 奖品模块错误码 --------


    // ------ 活动模块错误码 --------


    // ------ 抽奖错误码 --------




    @Getter
    @Setter
    Integer code;

    @Getter
    @Setter
    String msg;

    ControllerStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
