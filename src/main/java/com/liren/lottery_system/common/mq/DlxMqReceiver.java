package com.liren.lottery_system.common.mq;

import com.liren.lottery_system.common.config.DirectRabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RabbitListener(queues = DirectRabbitConfig.DLX_QUEUE_NAME)
public class DlxMqReceiver {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitHandler
    public void process(Map<String, String> msg) {
        log.info("DlxMqReceiver消费者接收到异常消息：{}", msg.toString());
        rabbitTemplate.convertAndSend(DirectRabbitConfig.EXCHANGE_NAME,
                                        DirectRabbitConfig.ROUTING,
                                      msg);
    }
}
