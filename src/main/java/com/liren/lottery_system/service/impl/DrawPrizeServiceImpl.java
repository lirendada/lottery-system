package com.liren.lottery_system.service.impl;

import com.liren.lottery_system.common.config.DirectRabbitConfig;
import com.liren.lottery_system.common.constant.Constants;
import com.liren.lottery_system.common.enums.ActivityStatusEnum;
import com.liren.lottery_system.common.enums.PrizeStatusEnum;
import com.liren.lottery_system.common.enums.ServiceStatusEnum;
import com.liren.lottery_system.common.exception.ServiceException;
import com.liren.lottery_system.common.pojo.dto.DrawPrizeRequestDTO;
import com.liren.lottery_system.common.pojo.entity.*;
import com.liren.lottery_system.common.utils.JsonUtil;
import com.liren.lottery_system.common.utils.RedisUtil;
import com.liren.lottery_system.mapper.*;
import com.liren.lottery_system.service.DrawPrizeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DrawPrizeServiceImpl implements DrawPrizeService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ActivityXmlMapper activityXmlMapper;

    @Autowired
    private ActivityPrizeXmlMapper activityPrizeXmlMapper;

    @Autowired
    private PrizeXmlMapper prizeXmlMapper;

    @Autowired
    private UserXmlMapper userXmlMapper;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WinnerRecordXmlMapper winnerRecordXmlMapper;

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
    public List<WinnerRecordEntity> saveDrawPrizeResult(DrawPrizeRequestDTO drawPrizeRequestDTO) {
        // 1. 查询中奖者、活动、奖品信息
        ActivityEntity activity = activityXmlMapper.getActivity(drawPrizeRequestDTO.getActivityId());
        ActivityPrizeEntity activityPrize = activityPrizeXmlMapper.getActivityPrize(drawPrizeRequestDTO.getActivityId(), drawPrizeRequestDTO.getPrizeId());
        PrizeEntity prize = prizeXmlMapper.getPrize(drawPrizeRequestDTO.getPrizeId());
        List<UserEntity> userEntities = userXmlMapper.listUserByIds(drawPrizeRequestDTO.getWinnerList()
                .stream()
                .map(DrawPrizeRequestDTO.Winner::getUserId)
                .collect(Collectors.toList()));

        // 2. 整合并保存中奖信息
        List<WinnerRecordEntity> winnerRecords = userEntities.stream()
                .map(user -> {
                    WinnerRecordEntity winnerRecord = new WinnerRecordEntity();
                    winnerRecord.setActivityId(activity.getId());
                    winnerRecord.setActivityName(activity.getActivityName());
                    winnerRecord.setPrizeId(activityPrize.getPrizeId());
                    winnerRecord.setPrizeName(prize.getName());
                    winnerRecord.setPrizeTier(activityPrize.getPrizeTiers());
                    winnerRecord.setWinnerId(user.getId());
                    winnerRecord.setWinnerName(user.getUserName());
                    winnerRecord.setWinnerEmail(user.getEmail());
                    winnerRecord.setWinnerPhoneNumber(user.getEmail());
                    winnerRecord.setWinningTime(drawPrizeRequestDTO.getWinningTime());
                    return winnerRecord;
                }).collect(Collectors.toList());
        winnerRecordXmlMapper.insertWinningRecord(winnerRecords);

        // 3. 缓存奖品维度的结果
        storeToRedis(drawPrizeRequestDTO.getActivityId() + "_" + drawPrizeRequestDTO.getPrizeId(),
                winnerRecords,
                Constants.WINNING_RECORDS_TIMEOUT);

        // 4. 缓存活动维度的结果（需要判断活动是否结束）
        if(activity.getStatus().equalsIgnoreCase(ActivityStatusEnum.DONE.name())) {
            List<WinnerRecordEntity> winnerRecordEntities = winnerRecordXmlMapper.listWinnerRecordById(activityPrize.getActivityId());
            storeToRedis(String.valueOf(drawPrizeRequestDTO.getActivityId()),
                    winnerRecordEntities,
                    Constants.WINNING_RECORDS_TIMEOUT);
        }

        return winnerRecords;
    }

    @Override
    public void deleteWinningRecord(Long activityId, Long prizeId) {
        if(activityId == null) {
            log.warn("要删除的winning_record的相关活动id为空！");
            return;
        }

        // 回滚获奖记录
        winnerRecordXmlMapper.deleteWinningRecordById(activityId, prizeId);

        // 删除奖品维度和活动维度的缓存
        if(prizeId != null) {
            deleteFromRedis(activityId + "_" + prizeId);
        }
        deleteFromRedis(String.valueOf(activityId));
    }


    private void deleteFromRedis(String key) {
        try {
            if(redisUtil.hasKey(Constants.WINNING_RECORDS_PREFIX + key)) {
                redisUtil.delete(Constants.WINNING_RECORDS_PREFIX + key);
            }
        } catch (Exception e) {
            log.error("删除缓存中奖记录异常！key:{}", Constants.WINNING_RECORDS_PREFIX + key);
        }
    }

    private void storeToRedis(String key, List<WinnerRecordEntity> value, Long expireTime) {
        String str = "";
        try {
            if(!StringUtils.hasText(key) || CollectionUtils.isEmpty(value)) {
                log.warn("要缓存的内容为空！key:{}, value:{}", key, JsonUtil.toJson(value));
                return;
            }

            str = JsonUtil.toJson(value);
            redisUtil.set(Constants.WINNING_RECORDS_PREFIX + key, str, expireTime);
        } catch (Exception e) {
            log.error("缓存中奖记录异常！key:{}, value:{}", Constants.WINNING_RECORDS_PREFIX + key, str);
        }
    }

    private List<WinnerRecordEntity> getFromRedis(String key) {
        try {
            if (!StringUtils.hasText(key)) {
                log.warn("要从缓存中查询中奖记录的key为空！");
                return Arrays.asList();
            }

            String str = (String)redisUtil.get(Constants.WINNING_RECORDS_PREFIX + key);
            if (!StringUtils.hasText(str)) {
                return Arrays.asList();
            }
            return JsonUtil.toList(str, WinnerRecordEntity.class);
        } catch (Exception e) {
            log.error("从缓存中查询中奖记录异常！key:{}", Constants.WINNING_RECORDS_PREFIX + key);
            return Arrays.asList();
        }
    }



    @Override
    public boolean checkMqMessage(DrawPrizeRequestDTO data) {
        ActivityEntity activity = activityXmlMapper.getActivity(data.getActivityId());
        ActivityPrizeEntity activityPrize = activityPrizeXmlMapper.getActivityPrize(data.getActivityId(), data.getPrizeId());

        // 活动是否存在
        if(activity == null) {
            log.info("校验抽奖请求失败！失败原因：{}", ServiceStatusEnum.ACTIVITY_NOT_FOUND_ERROR.getMsg());
            return false;
        }

        // 活动是否有效
        if(activity.getStatus().equalsIgnoreCase(ActivityStatusEnum.DONE.name())) {
            log.info("校验抽奖请求失败！失败原因：{}", ServiceStatusEnum.ACTIVITY_INVALIDATED.getMsg());
            return false;
        }

        // 奖品是否存在
        if(activityPrize == null) {
            log.info("校验抽奖请求失败！失败原因：{}", ServiceStatusEnum.ACTIVITY_PRIZE_NOT_FOUND_ERROR.getMsg());
            return false;
        }

        // 奖品是否有效
        if(activityPrize.getStatus().equalsIgnoreCase(PrizeStatusEnum.DONE.name())) {
            log.info("校验抽奖请求失败！失败原因：{}", ServiceStatusEnum.ACTIVITY_PRIZE_INVALIDATED.getMsg());
            return false;
        }

        // 中奖人数是否和奖品数量一致
        if(activityPrize.getPrizeAmount() != data.getWinnerList().size()) {
            log.info("校验抽奖请求失败！失败原因：{}", ServiceStatusEnum.PRIZE_NOT_EQUAL_USER_NUMBER.getMsg());
            return false;
        }

        return true;
    }
}
