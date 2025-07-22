package com.liren.lottery_system.service.impl;

import com.liren.lottery_system.common.config.DirectRabbitConfig;
import com.liren.lottery_system.common.pojo.dto.DrawPrizeRequestDTO;
import com.liren.lottery_system.common.utils.JsonUtil;
import com.liren.lottery_system.service.DrawPrizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class DrawPrizeServiceImpl implements DrawPrizeService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void drawPrize(DrawPrizeRequestDTO req) {
        // 发送到rabbitmq
        Map<String, String> msg = new HashMap<>();
        msg.put("messageId", String.valueOf(UUID.randomUUID()));
        msg.put("messageData", JsonUtil.toJson(req));
        rabbitTemplate.convertAndSend(DirectRabbitConfig.EXCHANGE_NAME,
                                      DirectRabbitConfig.ROUTING,
                                      msg);
        log.info("rabbitmq发送消息成功，map={}", JsonUtil.toJson(msg));
    }
}
