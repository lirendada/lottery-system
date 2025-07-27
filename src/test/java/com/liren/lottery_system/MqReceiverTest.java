package com.liren.lottery_system;

import com.liren.lottery_system.common.enums.ActivityStatusEnum;
import com.liren.lottery_system.common.enums.PrizeStatusEnum;
import com.liren.lottery_system.common.enums.UserStatusEnum;
import com.liren.lottery_system.common.mq.MqReceiver;
import com.liren.lottery_system.common.pojo.dto.ConvertStatusDTO;
import com.liren.lottery_system.common.pojo.dto.DrawPrizeRequestDTO;
import com.liren.lottery_system.service.ConvertStatusService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.stream.Collectors;

@SpringBootTest
public class MqReceiverTest {
    @Autowired
    private ConvertStatusService convertStatusService;

    @Test
    void init() {
        ConvertStatusDTO convertStatusDTO = new ConvertStatusDTO();
        convertStatusDTO.setActivityId(27L);
        convertStatusDTO.setActivityStatus(ActivityStatusEnum.DONE);
        convertStatusDTO.setPrizeId(21L);
        convertStatusDTO.setPrizeStatus(PrizeStatusEnum.DONE);
        convertStatusDTO.setUserIds(
                Arrays.asList(53L, 54L)
        );
        convertStatusDTO.setUserStatus(UserStatusEnum.DONE);


        convertStatusService.convertStatus(convertStatusDTO);
    }

}
