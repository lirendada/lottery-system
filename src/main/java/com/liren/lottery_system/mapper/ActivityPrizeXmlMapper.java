package com.liren.lottery_system.mapper;


import com.liren.lottery_system.common.pojo.entity.ActivityPrizeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActivityPrizeXmlMapper {
    Integer batchInsert(List<ActivityPrizeEntity> activityPrize);
}
