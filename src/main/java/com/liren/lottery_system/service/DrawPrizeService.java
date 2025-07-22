package com.liren.lottery_system.service;

import com.liren.lottery_system.common.pojo.dto.DrawPrizeRequestDTO;

public interface DrawPrizeService {
    void drawPrize(DrawPrizeRequestDTO req);

    void checkMqMessage(DrawPrizeRequestDTO data);
}
