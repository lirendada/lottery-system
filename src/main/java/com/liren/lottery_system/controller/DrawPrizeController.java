package com.liren.lottery_system.controller;

import com.liren.lottery_system.common.pojo.dto.DrawPrizeRequestDTO;
import com.liren.lottery_system.common.pojo.dto.WinningRecordsRequestDTO;
import com.liren.lottery_system.common.pojo.entity.WinnerRecordEntity;
import com.liren.lottery_system.common.pojo.vo.WinningRecordsResponseVO;
import com.liren.lottery_system.common.utils.BeanTransformUtil;
import com.liren.lottery_system.common.utils.JsonUtil;
import com.liren.lottery_system.service.DrawPrizeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class DrawPrizeController {
    @Resource(name = "drawPrizeServiceImpl")
    private DrawPrizeService drawPrizeService;

    @PostMapping("/draw-prize")
    public Boolean drawPrize(@RequestBody @Validated DrawPrizeRequestDTO req) {
        log.info("drawPrize controller，DrawPrizeRequestDTO={}", JsonUtil.toJson(req));
        drawPrizeService.drawPrize(req);
        return true;
    }

    @PostMapping("/winning-records/show")
    public List<WinningRecordsResponseVO> getWinningRecords(@RequestBody @Validated WinningRecordsRequestDTO req) {
        log.info("getWinningRecords controller，WinningRecordsRequestDTO={}", JsonUtil.toJson(req));
        List<WinnerRecordEntity> winningRecords = drawPrizeService.getWinningRecords(req);
        return BeanTransformUtil.transToWinningRecordsResponseVO(winningRecords);
    }
}
