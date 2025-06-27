package com.liren.lottery_system.common.enums;

import com.liren.lottery_system.model.ErrorCode;
import lombok.Getter;
import lombok.Setter;

public enum ControllerStatusEnum {
    // ------ 人员模块错误码 --------

    REGISTER_ERROR(100, "注册失败"),
    LOGIN_ERROR(101, "登录失败"),

    // ------ 奖品模块错误码 --------

    FIND_PRIZE_LIST_ERROR(200, "查询奖品列表失败"),

    // ------ 活动模块错误码 --------

    CREATE_ACTIVITY_ERROR(300, "创建活动失败"),
    FIND_ACTIVITY_LIST_ERROR(301, "查询活动列表失败"),
    GET_ACTIVITY_DETAIL_ERROR(302, "查询活动详细信息失败");

    // ------ 抽奖错误码 --------




    @Getter @Setter
    ErrorCode errorCode;

    ControllerStatusEnum(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    ControllerStatusEnum(Integer code, String msg) {
        this.errorCode.setCode(code);
        this.errorCode.setErrMsg(msg);
    }
}
