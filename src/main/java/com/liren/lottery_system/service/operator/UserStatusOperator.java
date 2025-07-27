package com.liren.lottery_system.service.operator;

import com.liren.lottery_system.common.enums.PrizeStatusEnum;
import com.liren.lottery_system.common.enums.UserStatusEnum;
import com.liren.lottery_system.common.pojo.dto.ConvertStatusDTO;
import com.liren.lottery_system.common.pojo.entity.ActivityUserEntity;
import com.liren.lottery_system.mapper.ActivityUserXmlMapper;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class UserStatusOperator extends AbstractStatusOperator {
    @Autowired
    private ActivityUserXmlMapper activityUserXmlMapper;

    @Override
    public Integer sequence() {
        return 1;
    }

    @Override
    public Boolean needConvert(ConvertStatusDTO convertStatusDTO) {
        // 校验
        Long activityId = convertStatusDTO.getActivityId();
        List<Long> userIds = convertStatusDTO.getUserIds();
        UserStatusEnum userStatus = convertStatusDTO.getUserStatus();

        if(activityId == null || Collections.isEmpty(userIds) || userStatus == null) {
            return false;
        }

        // 如果当前用户状态和目标状态是否一致，一致则不需要转换，因为一旦转变就是整体转变
        List<ActivityUserEntity> activityUserEntities = activityUserXmlMapper.listActivityUserById(activityId, userIds);
        if(CollectionUtils.isEmpty(activityUserEntities)) {
            return false;
        }

        for(ActivityUserEntity activityUser : activityUserEntities) {
            if(activityUser.getStatus().equalsIgnoreCase(userStatus.name())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Boolean convert(ConvertStatusDTO convertStatusDTO) {
        try {
            activityUserXmlMapper.updateUserStatus(convertStatusDTO.getActivityId(),
                    convertStatusDTO.getUserIds(),
                    convertStatusDTO.getUserStatus().name());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
