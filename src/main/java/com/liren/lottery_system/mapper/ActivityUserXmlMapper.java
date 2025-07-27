package com.liren.lottery_system.mapper;


import com.liren.lottery_system.common.pojo.entity.ActivityPrizeEntity;
import com.liren.lottery_system.common.pojo.entity.ActivityUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActivityUserXmlMapper {
    Integer batchInsert(List<ActivityUserEntity> activityUser);

    List<ActivityUserEntity> listActivityUser(Long activityId);

    List<ActivityUserEntity> listActivityUserById(Long activityId, List<Long> userIds);
}
