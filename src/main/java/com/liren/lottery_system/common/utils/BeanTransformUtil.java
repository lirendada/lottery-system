package com.liren.lottery_system.common.utils;

import com.liren.lottery_system.common.pojo.dto.LoginResponseDTO;
import com.liren.lottery_system.common.pojo.dto.RegisterResponseDTO;
import com.liren.lottery_system.common.pojo.vo.LoginResponseVO;
import com.liren.lottery_system.common.pojo.vo.RegisterResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

@Slf4j
public class BeanTransformUtil {
    /**
     * RegisterResponseDTO 转化为 RegisterResponseVO
     */
    public static RegisterResponseVO trans(RegisterResponseDTO responseDTO) {
        log.info("RegisterResponseDTO 转化为 RegisterResponseVO");
        RegisterResponseVO responseVO = new RegisterResponseVO();
        BeanUtils.copyProperties(responseDTO, responseVO);
        return responseVO;
    }

    /**
     * LoginResponseDTO 转化为 LoginResponseVO
     */
    public static LoginResponseVO trans(LoginResponseDTO responseDTO) {
        log.info("LoginResponseDTO 转化为 LoginResponseVO");
        LoginResponseVO responseVO = new LoginResponseVO();
        BeanUtils.copyProperties(responseDTO, responseVO);
        return responseVO;
    }

}
