package com.liren.lottery_system.mapper;

import com.liren.lottery_system.common.pojo.entity.WinnerRecordEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WinnerRecordXmlMapper {
    List<WinnerRecordEntity> listWinnerRecordById(Long activityId);
}
