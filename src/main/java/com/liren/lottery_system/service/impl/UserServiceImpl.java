package com.liren.lottery_system.service.impl;

import com.liren.lottery_system.common.constant.Constants;
import com.liren.lottery_system.common.enums.UserIdentityEnum;
import com.liren.lottery_system.common.exception.ServiceException;
import com.liren.lottery_system.common.pojo.dto.*;
import com.liren.lottery_system.common.enums.ServiceStatusEnum;
import com.liren.lottery_system.common.pojo.entity.Encrypt;
import com.liren.lottery_system.common.pojo.entity.UserEntity;
import com.liren.lottery_system.common.pojo.vo.UserResponseVO;
import com.liren.lottery_system.common.utils.JWTUtil;
import com.liren.lottery_system.common.utils.RedisUtil;
import com.liren.lottery_system.common.utils.RegexUtil;
import com.liren.lottery_system.common.utils.SecurityUtil;
import com.liren.lottery_system.mapper.UserXmlMapper;
import com.liren.lottery_system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserXmlMapper userXmlMapper;

    @Autowired
    private RedisUtil redisUtil;

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

    @Override
    public LoginResponseDTO loginPassword(LoginPasswordRequestDTO req) {
        // 校验用户信息
        String loginName = req.getLoginName(); // 邮箱或者手机
        UserEntity user = null;
        if(RegexUtil.checkMail(loginName)) {
            // 是合法邮箱
            user = userXmlMapper.getUserByEmail(loginName);
        } else if(RegexUtil.checkMobile(loginName)) {
            // 是合法手机
            user = userXmlMapper.getUserByPhone(new Encrypt(loginName));
        } else {
            // 到这说明两个都不是，则抛异常
            throw new ServiceException(ServiceStatusEnum.LOGIN_INFO_ERROR.getCodeStatus());
        }

        // 判断用户是否存在
        if(user == null) {
            throw new ServiceException(ServiceStatusEnum.USER_NOT_FOUND.getCodeStatus());
        }

        // 校验身份是否一致，不一致则抛异常
        if(!user.getIdentity().equalsIgnoreCase(req.getMandatoryIdentity())) {
            throw new ServiceException(ServiceStatusEnum.IDENTITY_INVALIDATED.getCodeStatus());
        }

        // 校验密码是否正确
        String password = req.getPassword();
        if(!SecurityUtil.isValidated(req.getPassword(), user.getPassword())) {
            throw new ServiceException(ServiceStatusEnum.PASSWORD_INVALIDATED.getCodeStatus());
        }

        // 生成token
        String token = JWTUtil.createJWT(Map.of("userId", user.getId(), "identity", user.getIdentity()));

        // 进行包装返回
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setIdentity(user.getIdentity());
        loginResponseDTO.setToken(token);
        return loginResponseDTO;
    }

    @Override
    public LoginResponseDTO loginVerificationCode(LoginVerificationCodeRequestDTO req) {
        // 校验手机
        if(!RegexUtil.checkMobile(req.getLoginMobile())) {
            throw new ServiceException(ServiceStatusEnum.PHONE_INVALIDATED.getCodeStatus());
        }

        // 校验验证码
        String verificationCode = (String)redisUtil.get(Constants.VERIFICATION_CODE_PREFIX + req.getLoginMobile());
        if(!StringUtils.hasText(verificationCode) || !verificationCode.equals(req.getVerificationCode())) {
            throw new ServiceException(ServiceStatusEnum.VERIFICATION_CODE_INVALIDATED.getCodeStatus());
        }

        // 校验身份是否一致，不一致则抛异常
        UserEntity user = userXmlMapper.getUserByPhone(new Encrypt(req.getLoginMobile()));
        if(user == null) {
            throw new ServiceException(ServiceStatusEnum.USER_NOT_FOUND.getCodeStatus());
        }
        if(!user.getIdentity().equalsIgnoreCase(req.getMandatoryIdentity())) {
            throw new ServiceException(ServiceStatusEnum.IDENTITY_INVALIDATED.getCodeStatus());
        }

        // 生成token
        String token = JWTUtil.createJWT(Map.of("userId", user.getId(), "identity", user.getIdentity()));

        // 进行包装返回
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setIdentity(user.getIdentity());
        loginResponseDTO.setToken(token);
        return loginResponseDTO;
    }

    @Override
    public List<UserEntity> getUserList() {
        return userXmlMapper.ListUser();
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
