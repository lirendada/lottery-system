package com.liren.lottery_system.service.impl;

import com.liren.lottery_system.common.config.DirectRabbitConfig;
import com.liren.lottery_system.common.enums.ActivityStatusEnum;
import com.liren.lottery_system.common.enums.PrizeStatusEnum;
import com.liren.lottery_system.common.enums.ServiceStatusEnum;
import com.liren.lottery_system.common.exception.ServiceException;
import com.liren.lottery_system.common.pojo.dto.DrawPrizeRequestDTO;
import com.liren.lottery_system.common.pojo.entity.ActivityEntity;
import com.liren.lottery_system.common.pojo.entity.ActivityPrizeEntity;
import com.liren.lottery_system.common.utils.JsonUtil;
import com.liren.lottery_system.mapper.ActivityPrizeXmlMapper;
import com.liren.lottery_system.mapper.ActivityXmlMapper;
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

    @Autowired
    private ActivityXmlMapper activityXmlMapper;

    @Autowired
    private ActivityPrizeXmlMapper activityPrizeXmlMapper;

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

    @Override
    public void checkMqMessage(DrawPrizeRequestDTO data) {
        ActivityEntity activity = activityXmlMapper.getActivity(data.getActivityId());
        ActivityPrizeEntity activityPrize = activityPrizeXmlMapper.getActivityPrize(data.getActivityId(), data.getPrizeId());

        // 活动是否存在
        if(activity == null) {
            throw new ServiceException(ServiceStatusEnum.ACTIVITY_NOT_FOUND_ERROR.getCodeStatus());
        }

        // 活动是否有效
        if(activity.getStatus().equalsIgnoreCase(ActivityStatusEnum.DONE.getMsg())) {
            throw new ServiceException(ServiceStatusEnum.ACTIVITY_INVALIDATED.getCodeStatus());
        }

        // 奖品是否存在
        if(activityPrize == null) {
            throw new ServiceException(ServiceStatusEnum.ACTIVITY_PRIZE_NOT_FOUND_ERROR.getCodeStatus());
        }

        // 奖品是否有效
        if(activityPrize.getStatus().equalsIgnoreCase(PrizeStatusEnum.DONE.getMes())) {
            throw new ServiceException(ServiceStatusEnum.ACTIVITY_PRIZE_INVALIDATED.getCodeStatus());
        }

        // 中奖人数是否和奖品数量一致
        if(activityPrize.getPrizeAmount() != data.getWinnerList().size()) {
            throw new ServiceException(ServiceStatusEnum.PRIZE_NOT_EQUAL_USER_NUMBER.getCodeStatus());
        }
    }
}
