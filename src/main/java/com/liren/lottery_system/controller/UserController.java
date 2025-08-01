package com.liren.lottery_system.controller;

import com.liren.lottery_system.common.exception.ControllerException;
import com.liren.lottery_system.common.pojo.dto.*;
import com.liren.lottery_system.common.pojo.entity.UserEntity;
import com.liren.lottery_system.common.pojo.vo.LoginResponseVO;
import com.liren.lottery_system.common.pojo.vo.RegisterResponseVO;
import com.liren.lottery_system.common.enums.ControllerStatusEnum;
import com.liren.lottery_system.common.pojo.vo.UserResponseVO;
import com.liren.lottery_system.common.utils.BeanTransformUtil;
import com.liren.lottery_system.common.utils.JsonUtil;
import com.liren.lottery_system.service.UserService;
import com.liren.lottery_system.service.VerificationCodeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    @Resource(name = "userServiceImpl")
    private UserService userService;

    @Resource(name = "verificationCodeServiceImpl")
    private VerificationCodeService verificationCodeService;

    /**
     * 注册
     */
    @PostMapping("/register")
    public RegisterResponseVO register(@RequestBody @Validated RegisterRequestDTO req) {
        log.info("register controller：" + JsonUtil.toJson(req));

        RegisterResponseDTO responseDTO = userService.register(req);
        if(responseDTO == null) {
            throw new ControllerException(ControllerStatusEnum.REGISTER_ERROR.getCodeStatus());
        }
        return BeanTransformUtil.trans(responseDTO);
    }

    /**
     * 发送验证码
     */
    @GetMapping("/verification-code/send")
    public String sendVerificationCode(String phoneNumber) {
        // 目前无法使用发短信服务，所以直接模拟发回验证码给前端弹窗响应
        log.info("sendVerificationCode controller：" + phoneNumber);
        return verificationCodeService.sendVerificationCode(phoneNumber);
    }

    /**
     * 密码登录
     */
    @PostMapping("/password/login")
    public LoginResponseVO loginPassword(@RequestBody @Validated LoginPasswordRequestDTO req) {
        log.info("loginPassword controller：" + JsonUtil.toJson(req));
        LoginResponseDTO loginResponseDTO = userService.loginPassword(req);
        if(loginResponseDTO == null) {
            throw new ControllerException(ControllerStatusEnum.LOGIN_PASSWORD_ERROR.getCodeStatus());
        }
        return BeanTransformUtil.trans(loginResponseDTO);
    }

    /**
     * 验证码登录（用随机数代替）
     */
    @PostMapping("/message/login")
    public LoginResponseVO loginVerificationCode(@RequestBody @Validated LoginVerificationCodeRequestDTO req) {
        log.info("loginVerificationCode controller：" + JsonUtil.toJson(req));
        LoginResponseDTO loginResponseDTO = userService.loginVerificationCode(req);
        if(loginResponseDTO == null) {
            throw new ControllerException(ControllerStatusEnum.LOGIN_VERIFICATION_CODE_ERROR.getCodeStatus());
        }
        return BeanTransformUtil.trans(loginResponseDTO);
    }

    /**
     * 获取用户列表
     */
    @GetMapping("/base-user/find-list")
    public List<UserResponseVO> getUserList() {
        log.info("getUserList controller");
        List<UserEntity> userList = userService.getUserList();
        return BeanTransformUtil.trans(userList);
    }
}
