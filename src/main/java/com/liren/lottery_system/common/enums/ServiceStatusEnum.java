package com.liren.lottery_system.common.enums;

import com.liren.lottery_system.common.pojo.CodeStatus;
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
    PASSWORD_INVALIDATED(107, "密码不合法"),
    INSERT_USER_ERROR(108, "插入用户失败"),
    LOGIN_INFO_ERROR(109, "登录用户信息不合法"),
    USER_NOT_FOUND(110, "未找到用户"),
    VERIFICATION_CODE_INVALIDATED(111, "验证码无效"),

    // ------ 奖品模块错误码 --------
    INSERT_PRIZE_ERROR(200, "插入奖品失败"),

    // ------ 活动模块错误码 --------


    // ------ 抽奖错误码 --------


    // ------ 图片错误码 --------
    UPLOAD_PHOTO_ERROR(500, "上传文件失败");



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
        this.codeStatus = new CodeStatus(code, msg);
    }
}
