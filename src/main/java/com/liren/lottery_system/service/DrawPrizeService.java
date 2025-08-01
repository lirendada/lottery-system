package com.liren.lottery_system.service;

import com.liren.lottery_system.common.pojo.dto.DrawPrizeRequestDTO;
import com.liren.lottery_system.common.pojo.dto.WinningRecordsRequestDTO;
import com.liren.lottery_system.common.pojo.entity.WinnerRecordEntity;

import java.util.List;

public interface DrawPrizeService {
    void drawPrize(DrawPrizeRequestDTO req);

    boolean checkMqMessage(DrawPrizeRequestDTO data);

    List<WinnerRecordEntity> saveDrawPrizeResult(DrawPrizeRequestDTO drawPrizeRequestDTO);

    void deleteWinningRecord(Long activityId, Long prizeId);

    List<WinnerRecordEntity> getWinningRecords(WinningRecordsRequestDTO req);
}
