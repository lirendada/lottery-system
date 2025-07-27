package com.liren.lottery_system.common.mq;

import com.liren.lottery_system.common.config.DirectRabbitConfig;
import com.liren.lottery_system.common.enums.ActivityStatusEnum;
import com.liren.lottery_system.common.enums.PrizeStatusEnum;
import com.liren.lottery_system.common.enums.UserStatusEnum;
import com.liren.lottery_system.common.exception.ServiceException;
import com.liren.lottery_system.common.pojo.dto.ConvertStatusDTO;
import com.liren.lottery_system.common.pojo.dto.DrawPrizeRequestDTO;
import com.liren.lottery_system.common.utils.JsonUtil;
import com.liren.lottery_system.service.ConvertStatusService;
import com.liren.lottery_system.service.DrawPrizeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RabbitListener(queues = DirectRabbitConfig.QUEUE_NAME)
public class MqReceiver {
    @Autowired
    private DrawPrizeService drawPrizeService;

    @Resource(name = "convertStatusServiceImpl")
    private ConvertStatusService convertStatusService;

    /**
     * 接收mq中的消息，进行抽奖异步处理
     */
    @RabbitHandler
    public void process(Map<String, String> msg) {
        log.info("DirectReceiver消费者接收到mq消息：{}", msg.toString());
        String data = msg.get("messageData");
        DrawPrizeRequestDTO drawPrizeRequestDTO = JsonUtil.toObject(data, DrawPrizeRequestDTO.class);

        try {
            // 1. 校验
            drawPrizeService.checkMqMessage(drawPrizeRequestDTO);

            // 2. 扭转活动状态（重要！！ 设计模式）
            convertStatus(drawPrizeRequestDTO);

            // 3. 保存抽奖结果

            // 4. 通知中奖人
        } catch (ServiceException e) {
            // 异常回滚中奖结果+活动/奖品状态，保证事务一致性
            log.error("mq消息处理异常：{}，{}", e.getCode(), e.getMsg(), e);
        } catch (Exception e) {
            // 异常回滚中奖结果+活动/奖品状态，保证事务一致性
            log.error("mq消息处理异常：",  e);
        }
    }


    public void convertStatus(DrawPrizeRequestDTO drawPrizeRequestDTO) {
        ConvertStatusDTO convertStatusDTO = new ConvertStatusDTO();
        convertStatusDTO.setActivityId(drawPrizeRequestDTO.getActivityId());
        convertStatusDTO.setActivityStatus(ActivityStatusEnum.DONE);
        convertStatusDTO.setPrizeId(drawPrizeRequestDTO.getPrizeId());
        convertStatusDTO.setPrizeStatus(PrizeStatusEnum.DONE);
        convertStatusDTO.setUserIds(
                drawPrizeRequestDTO.getWinnerList().stream()
                        .map(DrawPrizeRequestDTO.Winner::getUserId)
                        .collect(Collectors.toList())
        );
        convertStatusDTO.setUserStatus(UserStatusEnum.DONE);

        convertStatusService.convertStatus(convertStatusDTO);
    }
}
