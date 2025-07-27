package com.liren.lottery_system.service.operator;

import com.liren.lottery_system.common.enums.ActivityStatusEnum;
import com.liren.lottery_system.common.enums.PrizeStatusEnum;
import com.liren.lottery_system.common.enums.ServiceStatusEnum;
import com.liren.lottery_system.common.exception.ServiceException;
import com.liren.lottery_system.common.pojo.dto.ConvertStatusDTO;
import com.liren.lottery_system.common.pojo.entity.ActivityEntity;
import com.liren.lottery_system.common.pojo.entity.ActivityPrizeEntity;
import com.liren.lottery_system.mapper.ActivityPrizeXmlMapper;
import com.liren.lottery_system.mapper.ActivityXmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ActivityStatusOperator extends AbstractStatusOperator {
    @Autowired
    private ActivityXmlMapper activityXmlMapper;

    @Autowired
    private ActivityPrizeXmlMapper activityPrizeXmlMapper;

    @Override
    public Integer sequence() {
        return 2;
    }

    @Override
    public Boolean needConvert(ConvertStatusDTO convertStatusDTO) {
        // 校验
        Long activityId = convertStatusDTO.getActivityId();
        ActivityStatusEnum activityStatus = convertStatusDTO.getActivityStatus();
        if(activityId == null || activityStatus == null) {
            return false;
        }


        ActivityEntity activity = activityXmlMapper.getActivity(activityId);
        if(activity == null) {
            return false;
        }

        // 当前活动状态与传入的状态一致，则不需要转换
        if(activity.getStatus().equalsIgnoreCase(activityStatus.name())) {
            return false;
        }

        // 如果奖品还没全部抽完，则不需要转换
        List<String> statusList = activityPrizeXmlMapper.listActivityPrize(activityId)
                .stream()
                .map(ActivityPrizeEntity::getStatus)
                .collect(Collectors.toList());
        for(String status : statusList) {
            if(status.equalsIgnoreCase(PrizeStatusEnum.AVAILABLE.name())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Boolean convert(ConvertStatusDTO convertStatusDTO) {

    }
}
