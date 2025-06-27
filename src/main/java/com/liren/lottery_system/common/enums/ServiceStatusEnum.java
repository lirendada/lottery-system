package com.liren.lottery_system.common.enums;

import com.liren.lottery_system.model.ErrorCode;
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
    ErrorCode errorCode;

    ServiceStatusEnum(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    ServiceStatusEnum(Integer code, String msg) {
        this.errorCode.setCode(code);
        this.errorCode.setErrMsg(msg);
    }
}
