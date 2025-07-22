package com.liren.lottery_system.mapper;


import com.liren.lottery_system.common.pojo.entity.ActivityPrizeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActivityPrizeXmlMapper {
    /**
     * 插入活动奖品队列
     */
    Integer batchInsert(List<ActivityPrizeEntity> activityPrize);

    /**
     * 获取活动奖品列表
     */
    List<ActivityPrizeEntity> listActivityPrize(Long activityId);
}
