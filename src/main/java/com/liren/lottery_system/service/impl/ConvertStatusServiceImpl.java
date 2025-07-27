package com.liren.lottery_system.service.impl;

import com.liren.lottery_system.common.enums.ServiceStatusEnum;
import com.liren.lottery_system.common.exception.ServiceException;
import com.liren.lottery_system.common.pojo.dto.ConvertStatusDTO;
import com.liren.lottery_system.service.ConvertStatusService;
import com.liren.lottery_system.service.operator.AbstractStatusOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ConvertStatusServiceImpl implements ConvertStatusService {
    @Autowired
    private final Map<String, AbstractStatusOperator> statusOperator = new HashMap<>();

    /**
     * 💥责任链模式 + 策略模式
     * 作用：提高模块依赖可控性、可维护性、可拓展性等
     */
    @Override
    public void convertStatus(ConvertStatusDTO convertStatusDTO) {
        if(CollectionUtils.isEmpty(statusOperator)) {
            log.warn("operatorMap为空！");
            return;
        }

        Map<String, AbstractStatusOperator> curOperator = new HashMap<>(statusOperator);
        Boolean isUpdate = false;

        // 1. 扭转奖品、人员状态
        isUpdate = processConvert(convertStatusDTO, curOperator, 1);

        // 2. 扭转活动状态（依赖于奖品状态是否全部扭转）
        isUpdate = processConvert(convertStatusDTO, curOperator, 2) || isUpdate;

        // 3. 更新缓存
        if(isUpdate) {

        }
    }

    private Boolean processConvert(ConvertStatusDTO convertStatusDTO,
                                    Map<String, AbstractStatusOperator> curOperator,
                                    int sequence) {
        Boolean isUpdate = false;

        // 遍历map
        for(Map.Entry<String, AbstractStatusOperator> entry : curOperator.entrySet()) {
            AbstractStatusOperator value = entry.getValue();

            // 校验，如果序列不同，或者不需要转换，则跳过
            if(!value.sequence().equals(sequence)
                || !value.needConvert(convertStatusDTO)) {
                continue;
            }

            // 进行转换，不成功则抛异常
            if(!value.convert(convertStatusDTO)) {
                log.error("{}状态转换失败！", value.getClass().getName());
                throw new ServiceException(ServiceStatusEnum.CONVERT_STATUS_ERROR.getCodeStatus());
            }

            // 到这说明转换成功，删除当前处理的operator，提高执行效率（可选）
            curOperator.remove(entry.getKey());
            isUpdate = true;
        }

        // 返回转换结果
        return isUpdate;
    }
}
