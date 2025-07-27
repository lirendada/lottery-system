package com.liren.lottery_system.service.operator;

import com.liren.lottery_system.common.pojo.dto.ConvertStatusDTO;

public abstract class AbstractStatusOperator {
    public abstract Integer sequence();

    public abstract Boolean needConvert(ConvertStatusDTO convertStatusDTO);

    public abstract Boolean convert(ConvertStatusDTO convertStatusDTO);
}
