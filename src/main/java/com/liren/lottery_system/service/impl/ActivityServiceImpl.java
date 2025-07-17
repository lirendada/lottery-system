package com.liren.lottery_system.service.impl;

import com.liren.lottery_system.common.enums.PrizeTierEnum;
import com.liren.lottery_system.common.enums.ServiceStatusEnum;
import com.liren.lottery_system.common.exception.ServiceException;
import com.liren.lottery_system.common.pojo.dto.ActivityPrizeDTO;
import com.liren.lottery_system.common.pojo.dto.ActivityUserDTO;
import com.liren.lottery_system.common.pojo.dto.CreateActivityRequestDTO;
import com.liren.lottery_system.mapper.ActivityXmlMapper;
import com.liren.lottery_system.mapper.PrizeXmlMapper;
import com.liren.lottery_system.mapper.UserXmlMapper;
import com.liren.lottery_system.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityXmlMapper activityXmlMapper;

    @Autowired
    private PrizeXmlMapper prizeXmlMapper;

    @Autowired
    private UserXmlMapper userXmlMapper;

    @Override
    public Long createActivity(CreateActivityRequestDTO req) {
        // 校验参数合法性
        checkInfo(req);

        // 活动数据入库

        // 活动奖品数据入库

        // 活动人员数据入库

        // 整合活动详细数据，存放到redis中

        // 返回活动id
        return null;
    }


    private void checkInfo(CreateActivityRequestDTO req) {
        if(req == null) {
            throw new ServiceException(ServiceStatusEnum.PARAMETER_INVALIDATED.getCodeStatus());
        }

        // 该活动是否已经存在并且正在执行中（需要获取该活动的数量）
        Integer activityCount = activityXmlMapper.countActivityByName(req.getActivityName());
        if(activityCount != 0) {
            throw new ServiceException(ServiceStatusEnum.ACTIVITY_ALREADY_EXIST.getCodeStatus());
        }

        // 该活动的奖品是否都存在（需要获取这些奖品，然后判断是否一致）
        // 根据传入的参数，先转化为List<Long> ids，然后去库里看看是否有对应的ids，从而判断是否都存在
        List<Long> InputPrizeIds = req.getActivityPrizeList()
                .stream()
                .map(ActivityPrizeDTO::getPrizeId)
                .distinct()
                .collect(Collectors.toList());
        List<Long> prizeIds = prizeXmlMapper.listPrizeIdByIds(InputPrizeIds);
        if(CollectionUtils.isEmpty(prizeIds)) {
            throw new ServiceException(ServiceStatusEnum.ACTIVITY_PRIZE_NOT_FOUND.getCodeStatus());
        }
        for(Long id : InputPrizeIds) {
            if(!prizeIds.contains(id)) {
                throw new ServiceException(ServiceStatusEnum.ACTIVITY_PRIZE_NOT_FOUND.getCodeStatus());
            }
        }

        // 该活动的奖品级别是否正确
        req.getActivityPrizeList().forEach(x -> {
            if(null == PrizeTierEnum.forName(x.getPrizeTiers())) {
                throw new ServiceException(ServiceStatusEnum.ACTIVITY_PRIZE_TIER_ERROR.getCodeStatus());
            }
        });

        // 该活动的用户是否都存在
        List<Long> InputUserIds = req.getActivityUserList()
                .stream()
                .map(ActivityUserDTO::getUserId)
                .distinct()
                .collect(Collectors.toList());
        List<Long> userIds = userXmlMapper.listUserIdByIds(InputUserIds);
        if(CollectionUtils.isEmpty(userIds)) {
            throw new ServiceException(ServiceStatusEnum.ACTIVITY_USER_NOT_FOUND.getCodeStatus());
        }
        InputUserIds.forEach(id -> {
            if(!userIds.contains(id)) {
                throw new ServiceException(ServiceStatusEnum.ACTIVITY_USER_NOT_FOUND.getCodeStatus());
            }
        });

        // 要求该活动的用户数量 >= 奖品数量
        int userNum = req.getActivityUserList().size();
        long prizeNum = req.getActivityPrizeList()
                .stream()
                .mapToLong(ActivityPrizeDTO::getPrizeAmount)
                .sum();
        if(userNum < prizeNum) {
            throw new ServiceException(ServiceStatusEnum.USER_NUMBER_TOO_SMALL.getCodeStatus());
        }
    }
}
