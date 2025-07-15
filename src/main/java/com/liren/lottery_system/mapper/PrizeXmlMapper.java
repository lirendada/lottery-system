package com.liren.lottery_system.mapper;

import com.liren.lottery_system.common.pojo.entity.PrizeEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PrizeXmlMapper {
    int insertPrize(PrizeEntity prize);
}
