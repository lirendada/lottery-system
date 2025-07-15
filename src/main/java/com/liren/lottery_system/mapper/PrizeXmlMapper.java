package com.liren.lottery_system.mapper;

import com.liren.lottery_system.common.pojo.entity.PrizeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PrizeXmlMapper {
    /**
     * 添加奖品
     */
    int insertPrize(PrizeEntity prize);

    /**
     * 获取奖品总数量
     */
    int countPrize();

    /**
     * 获取对应页的奖品内容
     */
    List<PrizeEntity> listPrize(Integer currentPage, Integer pageSize);
}
