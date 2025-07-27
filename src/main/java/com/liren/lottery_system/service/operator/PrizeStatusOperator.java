package com.liren.lottery_system.service.operator;

import com.liren.lottery_system.common.enums.PrizeStatusEnum;
import com.liren.lottery_system.common.pojo.dto.ConvertStatusDTO;
import com.liren.lottery_system.common.pojo.entity.ActivityPrizeEntity;
import com.liren.lottery_system.mapper.ActivityPrizeXmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrizeStatusOperator extends AbstractStatusOperator {
    @Autowired
    private ActivityPrizeXmlMapper activityPrizeXmlMapper;

    @Override
    public Integer sequence() {
        return 1;
    }

    @Override
    public Boolean needConvert(ConvertStatusDTO convertStatusDTO) {
        // 校验
        Long activityId = convertStatusDTO.getActivityId();
        Long prizeId = convertStatusDTO.getPrizeId();
        PrizeStatusEnum prizeStatus = convertStatusDTO.getPrizeStatus();

        if(activityId == null || prizeId == null || prizeStatus == null) {
            return false;
        }

        ActivityPrizeEntity activityPrize = activityPrizeXmlMapper.getActivityPrize(activityId, prizeId);
        if(activityPrize == null) {
            return false;
        }

        // 如果当前奖品状态和目标状态是否一致，则不需要转换
        if(activityPrize.getStatus().equalsIgnoreCase(prizeStatus.name())) {
            return false;
        }

        return true;
    }

    @Override
    public Boolean convert(ConvertStatusDTO convertStatusDTO) {
        try {
            activityPrizeXmlMapper.updatePrizeStatus(convertStatusDTO.getActivityId(),
                    convertStatusDTO.getPrizeId(),
                    convertStatusDTO.getPrizeStatus().name());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
