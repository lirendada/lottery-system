package com.liren.lottery_system.common.utils;

import com.liren.lottery_system.common.pojo.dto.*;
import com.liren.lottery_system.common.pojo.entity.UserEntity;
import com.liren.lottery_system.common.pojo.entity.WinnerRecordEntity;
import com.liren.lottery_system.common.pojo.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

    /**
     * GetActivityResponseDTO 转化为 ActivityResponseVO
     */
    public static ActivityResponseVO trans(GetActivityResponseDTO responseDTO) {
        log.info("GetActivityResponseDTO 转化为 ActivityResponseVO");
        ActivityResponseVO responseVO = new ActivityResponseVO();
        BeanUtils.copyProperties(responseDTO, responseVO);
        return responseVO;
    }

    /**
     * GetActivityResponseDTO 转化为 ActivityDetailResponseVO
     */
    public static ActivityDetailResponseVO trans(ActivityDetailDTO responseDTO) {
        log.info("ActivityDetailDTO 转化为 ActivityDetailResponseVO");
        ActivityDetailResponseVO responseVO = new ActivityDetailResponseVO();
        responseVO.setActivityId(responseDTO.getActivityId());
        responseVO.setActivityName(responseDTO.getActivityName());
        responseVO.setDescription(responseDTO.getDescription());
        responseVO.setValid(responseDTO.isValid());

        // 抽奖顺序：一等奖、二等奖、三等奖
        responseVO.setPrizes(responseDTO.getPrizeDTOList()
                .stream()
                .sorted(Comparator.comparingInt(prizeDTO -> prizeDTO.getPrizeTiers().getCode()))
                .map(prizeDTO -> {
                    ActivityDetailResponseVO.Prize prize = new ActivityDetailResponseVO.Prize();
                    prize.setPrizeId(prizeDTO.getPrizeId());
                    prize.setName(prizeDTO.getName());
                    prize.setDescription(prizeDTO.getDescription());
                    prize.setPrice(prizeDTO.getPrice());
                    prize.setImageUrl(prizeDTO.getImageUrl());
                    prize.setPrizeAmount(prizeDTO.getPrizeAmount());
                    prize.setPrizeTierName(prizeDTO.getPrizeTiers().getMes());
                    prize.setValid(prizeDTO.isValid());
                    return prize;
                }).collect(Collectors.toList()));

        responseVO.setUsers(responseDTO.getUserDTOList()
                .stream()
                .map(userDTO -> {
                    ActivityDetailResponseVO.User user = new ActivityDetailResponseVO.User();
                    user.setUserId(userDTO.getUserId());
                    user.setUserName(userDTO.getUserName());
                    user.setValid(userDTO.isValid());
                    return user;
                }).collect(Collectors.toList()));
        return responseVO;
    }

    /**
     * List<WinnerRecordEntity> 转化为 List<WinningRecordsResponseVO>
     */
    public static List<WinningRecordsResponseVO> transToWinningRecordsResponseVO(List<WinnerRecordEntity> responseDTO) {
        log.info("List<WinnerRecordEntity> 转化为 List<WinningRecordsResponseVO>");
        return responseDTO.stream()
                .map(x -> {
                    WinningRecordsResponseVO winningRecordsResponseVO = new WinningRecordsResponseVO();
                    winningRecordsResponseVO.setWinnerId(x.getWinnerId());
                    winningRecordsResponseVO.setWinnerName(x.getWinnerName());
                    winningRecordsResponseVO.setPrizeName(x.getPrizeName());
                    winningRecordsResponseVO.setPrizeTier(x.getPrizeTier());
                    winningRecordsResponseVO.setWinningTime(x.getWinningTime());
                    return winningRecordsResponseVO;
                }).collect(Collectors.toList());
    }
}
