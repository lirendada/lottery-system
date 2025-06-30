package com.liren.lottery_system.service.impl;

import com.liren.lottery_system.common.enums.UserIdentityEnum;
import com.liren.lottery_system.common.exception.ServiceException;
import com.liren.lottery_system.common.pojo.dto.RegisterRequestDTO;
import com.liren.lottery_system.common.pojo.dto.RegisterResponseDTO;
import com.liren.lottery_system.common.enums.ServiceStatusEnum;
import com.liren.lottery_system.common.pojo.entity.Encrypt;
import com.liren.lottery_system.common.pojo.entity.UserEntity;
import com.liren.lottery_system.common.utils.JsonUtil;
import com.liren.lottery_system.common.utils.RegexUtil;
import com.liren.lottery_system.common.utils.SecurityUtil;
import com.liren.lottery_system.mapper.UserXmlMapper;
import com.liren.lottery_system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserXmlMapper userXmlMapper;

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO req) {
        // 校验注册信息（出现异常不会继续执行，直接中断退出）
        checkRegisterInfo(req);

        // 加密：手机号、密码
        UserEntity user = new UserEntity();
        user.setUserName(req.getName());
        user.setEmail(req.getMail());
        user.setIdentity(req.getIdentity());
        user.setPhoneNumber(new Encrypt(req.getPhoneNumber()));
        user.setPassword(SecurityUtil.sha256Encrypt(req.getPassword()));

        // 存储信息
        Integer count = userXmlMapper.insertUser(user);
        if(count != 1) {
            throw new ServiceException(ServiceStatusEnum.INSERT_USER_ERROR.getCodeStatus());
        }

        // 返回
        RegisterResponseDTO responseDTO = new RegisterResponseDTO();
        responseDTO.setUserId(user.getId());
        log.info("userId: " + user.getId());
        return responseDTO;
    }

    private void checkRegisterInfo(RegisterRequestDTO req) {
        // 判空
        if(req == null) {
            throw new ServiceException(ServiceStatusEnum.REGISTER_INFO_IS_EMPTY.getCodeStatus());
        }
        log.info("通过判空验证");

        // 校验邮箱格式
        if(!RegexUtil.checkMail(req.getMail())) {
            throw new ServiceException(ServiceStatusEnum.EMAIL_INVALIDATED.getCodeStatus());
        }
        log.info("通过校验邮箱格式验证");

        // 校验手机格式
        if(!RegexUtil.checkMobile(req.getPhoneNumber())) {
            throw new ServiceException(ServiceStatusEnum.PHONE_INVALIDATED.getCodeStatus());
        }
        log.info("通过校验手机格式验证");

        // 校验身份信息是否存在
        if(UserIdentityEnum.isValidated(req.getIdentity()) == false) {
            throw new ServiceException(ServiceStatusEnum.IDENTITY_INVALIDATED.getCodeStatus());
        }
        log.info("通过校验身份信息是否存在验证");

        // 校验管理员密码必填
        if(req.getIdentity().equalsIgnoreCase(UserIdentityEnum.ADMIN.getIdentity())
                    && !StringUtils.hasText(req.getPassword())) {
            throw new ServiceException(ServiceStatusEnum.ADMIN_PASSWORD_NOT_FILLED.getCodeStatus());
        }
        log.info("通过校验管理员密码必填验证");

        // 密码校验，至少6位
        if(StringUtils.hasText(req.getPassword())
             && !RegexUtil.checkPassword(req.getPassword())) {
            throw new ServiceException(ServiceStatusEnum.PASSWORD_INVALIDATED.getCodeStatus());
        }
        log.info("通过密码校验，至少6位验证");

        // 校验邮箱是否被使用
        Integer emailCount = userXmlMapper.countEmail(req.getMail());
        log.info("emailCount: " + emailCount);
        if(emailCount > 0) {
            throw new ServiceException(ServiceStatusEnum.EMAIL_ALREADY_EXIST.getCodeStatus());
        }
        log.info("通过校验邮箱是否被使用验证");

        // 校验手机是否被使用
        Integer phoneCount = userXmlMapper.countPhone(new Encrypt(req.getPhoneNumber()));
        log.info("phoneCount: " + phoneCount);
        if(phoneCount > 0) {
            throw new ServiceException(ServiceStatusEnum.PHONE_ALREADY_EXIST.getCodeStatus());
        }
        log.info("通过校验手机是否被使用验证");
    }
}
