package com.liren.lottery_system.service.impl;

import com.liren.lottery_system.common.enums.UserIdentityEnum;
import com.liren.lottery_system.common.exception.ServiceException;
import com.liren.lottery_system.common.pojo.dto.RegisterRequestDTO;
import com.liren.lottery_system.common.pojo.dto.RegisterResponseDTO;
import com.liren.lottery_system.common.enums.ServiceStatusEnum;
import com.liren.lottery_system.common.pojo.entity.Encrypt;
import com.liren.lottery_system.common.utils.RegexUtil;
import com.liren.lottery_system.mapper.UserXmlMapper;
import com.liren.lottery_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserXmlMapper userXmlMapper;

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO req) {
        // 校验注册信息（出现异常不会继续执行，直接中断退出）
        checkRegisterInfo(req);

        // 加密TODO

        // 存储信息TODO

        // 返回TODO
        return null;
    }

    private void checkRegisterInfo(RegisterRequestDTO req) {
        // 判空
        if(req == null) {
            throw new ServiceException(ServiceStatusEnum.REGISTER_INFO_IS_EMPTY.getCodeStatus());
        }

        // 校验邮箱格式
        if(!RegexUtil.checkMail(req.getMail())) {
            throw new ServiceException(ServiceStatusEnum.EMAIL_INVALIDATED.getCodeStatus());
        }

        // 校验手机格式
        if(!RegexUtil.checkMobile(req.getPhoneNumber())) {
            throw new ServiceException(ServiceStatusEnum.PHONE_INVALIDATED.getCodeStatus());
        }

        // 校验身份信息是否存在
        if(UserIdentityEnum.isValidated(req.getIdentity()) == false) {
            throw new ServiceException(ServiceStatusEnum.IDENTITY_INVALIDATED.getCodeStatus());
        }

        // 校验管理员密码必填
        if(req.getIdentity().equalsIgnoreCase(UserIdentityEnum.ADMIN.getIdentity())
                    && !StringUtils.hasText(req.getPassword())) {
            throw new ServiceException(ServiceStatusEnum.ADMIN_PASSWORD_NOT_FILLED.getCodeStatus());
        }

        // 密码校验，至少6位
        if(StringUtils.hasText(req.getPassword())
             && !RegexUtil.checkPassword(req.getPassword())) {
            throw new ServiceException(ServiceStatusEnum.PASSWORD_INVALIDATED.getCodeStatus());
        }

        // 校验邮箱是否被使用
        if(userXmlMapper.countEmail(req.getMail()) > 0) {
            throw new ServiceException(ServiceStatusEnum.EMAIL_ALREADY_EXIST.getCodeStatus());
        }

        // 校验手机是否被使用
        if(userXmlMapper.countPhone(new Encrypt(req.getPhoneNumber())) > 0) {
            throw new ServiceException(ServiceStatusEnum.EMAIL_ALREADY_EXIST.getCodeStatus());
        }
    }
}
