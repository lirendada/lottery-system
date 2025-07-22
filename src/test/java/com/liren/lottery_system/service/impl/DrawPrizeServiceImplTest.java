package com.liren.lottery_system.service.impl;

import com.liren.lottery_system.common.pojo.dto.DrawPrizeRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DrawPrizeServiceImplTest {
    @Autowired
    private DrawPrizeServiceImpl drawPrizeService;

    @Test
    void drawPrize() {
        DrawPrizeRequestDTO drawPrizeRequestDTO = new DrawPrizeRequestDTO();
        drawPrizeRequestDTO.setActivityId(1l);
        drawPrizeRequestDTO.setPrizeId(1l);
        drawPrizeRequestDTO.setPrizeTiers("FIRST_PRIZE");
        drawPrizeRequestDTO.setWinningTime(new Date(System.currentTimeMillis()));
        List<DrawPrizeRequestDTO.Winner> winnerList = new ArrayList<>();
        winnerList.add(new DrawPrizeRequestDTO.Winner(1l, "liren"));
        drawPrizeRequestDTO.setWinnerList(winnerList);


        drawPrizeService.drawPrize(drawPrizeRequestDTO);
    }
}