package com.liren.lottery_system.common.status;

import lombok.Getter;
import lombok.Setter;

public enum ServiceStatusEnum {
    // ------ 人员模块错误码 --------
;


    // ------ 奖品模块错误码 --------


    // ------ 活动模块错误码 --------




    // ------ 抽奖错误码 --------




    // ------ 图片错误码 --------




    @Getter
    @Setter
    Integer code;

    @Getter
    @Setter
    String msg;

    ServiceStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
