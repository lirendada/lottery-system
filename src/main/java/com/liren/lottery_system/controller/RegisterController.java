package com.liren.lottery_system.controller;

import com.liren.lottery_system.common.exception.ControllerException;
import com.liren.lottery_system.common.pojo.dto.RegisterRequestDTO;
import com.liren.lottery_system.common.pojo.dto.RegisterResponseDTO;
import com.liren.lottery_system.common.pojo.vo.RegisterResponseVO;
import com.liren.lottery_system.common.enums.ControllerStatusEnum;
import com.liren.lottery_system.common.utils.BeanTransformUtil;
import com.liren.lottery_system.common.utils.JsonUtil;
import com.liren.lottery_system.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RegisterController {
    @Resource(name = "userServiceImpl")
    private UserService userService;

    @PostMapping("/register")
    public RegisterResponseVO register(@RequestBody @Validated RegisterRequestDTO req) {
        log.info("register controller：" + JsonUtil.toJson(req));

        RegisterResponseDTO responseDTO = userService.register(req);
        if(responseDTO == null) {
            throw new ControllerException(ControllerStatusEnum.REGISTER_ERROR.getCodeStatus());
        }
        log.info("responseDTO: " + responseDTO);
        return BeanTransformUtil.trans(responseDTO);
    }
}
