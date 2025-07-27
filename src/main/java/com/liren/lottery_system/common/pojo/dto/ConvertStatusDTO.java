package com.liren.lottery_system.common.pojo.dto;

import com.liren.lottery_system.common.enums.ActivityStatusEnum;
import com.liren.lottery_system.common.enums.PrizeStatusEnum;
import com.liren.lottery_system.common.enums.UserStatusEnum;
import lombok.Data;

import java.util.List;

@Data
public class ConvertStatusDTO {
    private Long activityId;
    private ActivityStatusEnum activityStatus;

    private Long prizeId;
    private PrizeStatusEnum prizeStatus;

    private List<Long> userIds;
    private UserStatusEnum userStatus;
}
