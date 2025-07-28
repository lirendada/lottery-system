package com.liren.lottery_system.mapper;

import com.liren.lottery_system.common.pojo.entity.WinnerRecordEntity;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WinnerRecordXmlMapper {
    List<WinnerRecordEntity> listWinnerRecordById(Long activityId);

    Integer countWinnerRecord(Long activityId, Long prizeId);

    void deleteWinningRecordById(Long activityId, Long prizeId);
}
