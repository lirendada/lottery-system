package com.liren.lottery_system.service;

import com.liren.lottery_system.common.pojo.dto.ActivityDetailDTO;
import com.liren.lottery_system.common.pojo.dto.CreateActivityRequestDTO;
import com.liren.lottery_system.common.pojo.dto.GetActivityRequestDTO;
import com.liren.lottery_system.common.pojo.dto.GetActivityResponseDTO;

public interface ActivityService {
    Long createActivity(CreateActivityRequestDTO req);

    GetActivityResponseDTO getActivity(GetActivityRequestDTO req);

    ActivityDetailDTO getActivityDetail(Long activityId);

    /**
     * 更新redis中的活动详细信息
     * @param activityId
     */
    void updateActivityDetail(Long activityId);
}
