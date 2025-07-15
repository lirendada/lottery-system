package com.liren.lottery_system.common.utils;

import com.liren.lottery_system.common.pojo.dto.GetPrizeResponseDTO;
import com.liren.lottery_system.common.pojo.dto.LoginResponseDTO;
import com.liren.lottery_system.common.pojo.dto.RegisterResponseDTO;
import com.liren.lottery_system.common.pojo.entity.UserEntity;
import com.liren.lottery_system.common.pojo.vo.LoginResponseVO;
import com.liren.lottery_system.common.pojo.vo.PrizeResponseVO;
import com.liren.lottery_system.common.pojo.vo.RegisterResponseVO;
import com.liren.lottery_system.common.pojo.vo.UserResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * List<UserEntity> 转化为 List<UserResponseVO>
     */
    public static List<UserResponseVO> trans(List<UserEntity> userList) {
        log.info("List<UserEntity> 转化为 List<UserResponseVO>");
        if(CollectionUtils.isEmpty(userList)) {
            return Arrays.asList();
        }
//        List<UserResponseVO> responseVO = new ArrayList<>(userList.size());
//        for(int i = 0; i < userList.size(); ++i) {
//            BeanUtils.copyProperties(userList.get(i), responseVO.get(i));
//            responseVO.get(i).setUserId(userList.get(i).getId());
//        }
        return userList.stream()
                .map(x -> {
                    UserResponseVO responseVO = new UserResponseVO();
                    responseVO.setUserId(x.getId());
                    responseVO.setUserName(x.getUserName());
                    responseVO.setIdentity(x.getIdentity());
                    return responseVO;
                }).collect(Collectors.toList());
    }

    /**
     * GetPrizeResponseDTO 转化为 PrizeResponseVO
     */
    public static PrizeResponseVO trans(GetPrizeResponseDTO responseDTO) {
        log.info("GetPrizeResponseDTO 转化为 PrizeResponseVO");
        PrizeResponseVO responseVO = new PrizeResponseVO();
        BeanUtils.copyProperties(responseDTO, responseVO);
        return responseVO;
    }
}
