package com.liren.lottery_system.service.impl;

import com.liren.lottery_system.common.constant.Constants;
import com.liren.lottery_system.common.enums.ServiceStatusEnum;
import com.liren.lottery_system.common.exception.ServiceException;
import com.liren.lottery_system.common.utils.CaptchaUtil;
import com.liren.lottery_system.common.utils.RedisUtil;
import com.liren.lottery_system.common.utils.RegexUtil;
import com.liren.lottery_system.service.VerificationCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String sendVerificationCode(String phoneNumber) {
        // 校验手机号
        if(!RegexUtil.checkMobile(phoneNumber)) {
            throw new ServiceException(ServiceStatusEnum.PHONE_ALREADY_EXIST.getCodeStatus());
        }

        // 随机生成验证码
        String code = CaptchaUtil.generatorCode();
        log.info("创建随机验证码：{}", code);

        // 存储到redis中，返回验证码
        return redisUtil.set(Constants.VERIFICATION_CODE_PREFIX + phoneNumber, code, Constants.CAPTCHA_EXPIRE_TIME) == true ? code : null;
    }

    @Override
    public String getVerificationCode(String phoneNumber) {
        // 校验手机号
        if(!RegexUtil.checkMobile(phoneNumber)) {
            throw new ServiceException(ServiceStatusEnum.PHONE_ALREADY_EXIST.getCodeStatus());
        }

        // 返回验证码
        String code = (String)redisUtil.get(Constants.VERIFICATION_CODE_PREFIX + phoneNumber);
        System.out.println(code);
        return code;
    }
}
