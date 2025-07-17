package com.liren.lottery_system.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActivityXmlMapper {
    Integer countActivityByName(String activityName);

}
