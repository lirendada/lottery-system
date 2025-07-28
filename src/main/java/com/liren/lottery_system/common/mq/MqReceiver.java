package com.liren.lottery_system.common.mq;

import cn.hutool.core.date.DateUtil;
import com.liren.lottery_system.common.config.DirectRabbitConfig;
import com.liren.lottery_system.common.enums.ActivityStatusEnum;
import com.liren.lottery_system.common.enums.PrizeStatusEnum;
import com.liren.lottery_system.common.enums.PrizeTierEnum;
import com.liren.lottery_system.common.enums.UserStatusEnum;
import com.liren.lottery_system.common.exception.ServiceException;
import com.liren.lottery_system.common.pojo.dto.ConvertStatusDTO;
import com.liren.lottery_system.common.pojo.dto.DrawPrizeRequestDTO;
import com.liren.lottery_system.common.pojo.entity.ActivityPrizeEntity;
import com.liren.lottery_system.common.pojo.entity.WinnerRecordEntity;
import com.liren.lottery_system.common.utils.JsonUtil;
import com.liren.lottery_system.common.utils.MailUtil;
import com.liren.lottery_system.mapper.ActivityPrizeXmlMapper;
import com.liren.lottery_system.mapper.WinnerRecordXmlMapper;
import com.liren.lottery_system.service.ConvertStatusService;
import com.liren.lottery_system.service.DrawPrizeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RabbitListener(queues = DirectRabbitConfig.QUEUE_NAME)
public class MqReceiver {
    @Autowired
    private DrawPrizeService drawPrizeService;

    @Autowired
    private ActivityPrizeXmlMapper activityPrizeXmlMapper;

    @Resource(name = "convertStatusServiceImpl")
    private ConvertStatusService convertStatusService;

    @Autowired
    private ThreadPoolTaskExecutor asyncServiceExecutor;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private WinnerRecordXmlMapper winnerRecordXmlMapper;

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
            if(!drawPrizeService.checkMqMessage(drawPrizeRequestDTO)) {
                return;
            }

            // 2. 扭转状态（重要！！ 设计模式）
            convertStatus(drawPrizeRequestDTO);

            // 3. 保存抽奖结果到 winning_record 表中
            List<WinnerRecordEntity> winnerRecordEntities = drawPrizeService.saveDrawPrizeResult(drawPrizeRequestDTO);

            // 4. 通知中奖人
            asyncServiceExecutor.execute(() -> pushWinningList(winnerRecordEntities));

        } catch (ServiceException e) {
            // 异常回滚中奖结果+活动/奖品状态，保证事务一致性
            log.error("mq消息处理异常：{}，{}", e.getCode(), e.getMsg(), e);
            rollback(drawPrizeRequestDTO); // 进行回滚
            throw e; // 抛出异常，交给死信队列重发

        } catch (Exception e) {
            // 异常回滚中奖结果+活动/奖品状态，保证事务一致性
            log.error("mq消息处理异常：",  e);
            rollback(drawPrizeRequestDTO); // 进行回滚
            throw e; // 抛出异常，交给死信队列重发
        }
    }

    /**
     * 异常回滚
     */
    private void rollback(DrawPrizeRequestDTO drawPrizeRequestDTO) {
        // 判断活动、奖品、人员是否需要回滚
        if(!statusNeedRollBack(drawPrizeRequestDTO)) {
            return;
        }
        rollbackStatus(drawPrizeRequestDTO);

        // 判断 winning_record 记录是否需要回滚
        if(!winningRecordNeedRollBack(drawPrizeRequestDTO)) {
            return;
        }
        rollbackWinningRecord(drawPrizeRequestDTO);
    }

    private void rollbackWinningRecord(DrawPrizeRequestDTO drawPrizeRequestDTO) {
        drawPrizeService.deleteWinningRecord(drawPrizeRequestDTO.getActivityId(), drawPrizeRequestDTO.getPrizeId());
    }

    private boolean winningRecordNeedRollBack(DrawPrizeRequestDTO drawPrizeRequestDTO) {
        // 判断库中是否存在该获奖记录，存在则需要回滚
        Integer count = winnerRecordXmlMapper.countWinnerRecord(drawPrizeRequestDTO.getActivityId(), drawPrizeRequestDTO.getPrizeId());
        return count > 0;
    }

    private void rollbackStatus(DrawPrizeRequestDTO drawPrizeRequestDTO) {
        ConvertStatusDTO convertStatusDTO = new ConvertStatusDTO();
        convertStatusDTO.setActivityId(drawPrizeRequestDTO.getActivityId());
        convertStatusDTO.setActivityStatus(ActivityStatusEnum.RUNNING);
        convertStatusDTO.setPrizeId(drawPrizeRequestDTO.getPrizeId());
        convertStatusDTO.setPrizeStatus(PrizeStatusEnum.AVAILABLE);
        convertStatusDTO.setUserIds(drawPrizeRequestDTO.getWinnerList().stream().map(DrawPrizeRequestDTO.Winner::getUserId).collect(Collectors.toList()));
        convertStatusDTO.setUserStatus(UserStatusEnum.AVAILABLE);

        convertStatusService.rollbackStatus(convertStatusDTO);
    }

    private boolean statusNeedRollBack(DrawPrizeRequestDTO drawPrizeRequestDTO) {
        // 只需要判断人员或者奖品是否为DONE，是的话就需要回滚
        ActivityPrizeEntity activityPrize = activityPrizeXmlMapper.getActivityPrize(drawPrizeRequestDTO.getActivityId(), drawPrizeRequestDTO.getPrizeId());
        return activityPrize.getStatus().equalsIgnoreCase(PrizeStatusEnum.DONE.name());
    }

    /**
     * 发送邮箱
     */
    private void pushWinningList(List<WinnerRecordEntity> recordDOList) {
        if(CollectionUtils.isEmpty(recordDOList)) {
            log.warn("中奖名单为空！");
            return;
        }
        recordDOList.forEach(record -> {
            String context = "Hi，" + record.getWinnerName() + "。恭喜你在"
                    + record.getActivityName() + "活动中获得"
                    + PrizeTierEnum.forName(record.getPrizeTier())
                    + "：" + record.getPrizeName() + "。获奖时间为"
                    + DateUtil.formatTime(record.getWinningTime()) + "，请尽快领取您的奖励！";
            mailUtil.sendSampleMail(record.getWinnerEmail(),
                    "中奖通知",
                    context);
        });
    }

    /**
     * 扭转状态
     */
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
