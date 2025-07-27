package com.liren.lottery_system.service.operator;

import com.liren.lottery_system.common.pojo.dto.ConvertStatusDTO;
import org.springframework.stereotype.Component;

@Component
public class PrizeStatusOperator extends AbstractStatusOperator {
    @Override
    public Integer sequence() {
        return 1;
    }

    @Override
    public Boolean needConvert(ConvertStatusDTO convertStatusDTO) {
        // 校验
        // 如果奖品已经抽过，则不需要转换
        return null;
    }

    @Override
    public Boolean convert(ConvertStatusDTO convertStatusDTO) {

    }
}
