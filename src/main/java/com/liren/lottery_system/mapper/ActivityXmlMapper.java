package com.liren.lottery_system.mapper;

import com.liren.lottery_system.common.pojo.entity.ActivityEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActivityXmlMapper {
    /**
     * 根据活动名称得到该有效活动的数量，用于判断活动是否已经存在
     */
    Integer countActivityByName(String activityName);

    /**
     * 获取有效活动数量
     */
    Integer countActivity();

    /**
     * 新增活动
     */
    Integer insertActivity(ActivityEntity activity);

    /**
     * 获取活动列表
     */
    List<ActivityEntity> listActivity(Integer offset, Integer pageSize);

    /**
     * 获取活动
     */
    ActivityEntity getActivity(Long activityId);

    /**
     * 更新活动的状态
     */
    Integer updateActivityStatus(Long activityId, String status);
}
