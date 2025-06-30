package com.liren.lottery_system.common.enums;

import com.liren.lottery_system.common.exception.CodeStatus;
import lombok.Getter;
import lombok.Setter;

public enum ServiceStatusEnum {
    // ------ 用户模块错误码 --------
    REGISTER_INFO_IS_EMPTY(100, "注册信息为空"),
    EMAIL_INVALIDATED(101, "邮箱格式不合法"),
    EMAIL_ALREADY_EXIST(102, "邮箱已存在"),
    PHONE_INVALIDATED(103, "手机格式不合法"),
    PHONE_ALREADY_EXIST(104, "手机已存在"),
    IDENTITY_INVALIDATED(105, "身份信息不合法"),
    ADMIN_PASSWORD_NOT_FILLED(106, "管理员未填写密码"),
    PASSWORD_INVALIDATED(107, "密码格式不合法");

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

    @Getter
    @Setter
    CodeStatus codeStatus;

    ServiceStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
