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
    PAGE_PARAMETER_ERROR(201, "分页参数不能为空"),
    GET_PRIZE_LIST_ERROR(202, "分页获取奖品内容为空或为null"),
    PAGE_OVERMAX_ERROR(203, "当前页码已超出数据范围"),

    // ------ 活动模块错误码 --------
    PARAMETER_INVALIDATED(300, "请求参数不合法"),
    ACTIVITY_ALREADY_EXIST(301, "活动已存在"),
    ACTIVITY_PRIZE_NOT_FOUND(302, "活动奖品不存在"),
    ACTIVITY_PRIZE_TIER_ERROR(303, "活动奖品级别不存在"),
    ACTIVITY_USER_NOT_FOUND(304, "活动用户不存在"),
    USER_NUMBER_TOO_SMALL(305, "用户数量比奖品数量少"),
    ACTIVITY_NOT_FOUND(306, "该活动不存在"),

    // ------ 抽奖错误码 --------
    ACTIVITY_NOT_FOUND_ERROR(400, "该活动不存在"),
    ACTIVITY_PRIZE_NOT_FOUND_ERROR(401, "活动奖品不存在"),
    ACTIVITY_INVALIDATED(402, "活动无效/已执行完毕"),
    PRIZE_NOT_EQUAL_USER_NUMBER(403, "奖品和人员数量不一致"),
    ACTIVITY_PRIZE_INVALIDATED(404, "奖品无效/已执行完毕"),


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