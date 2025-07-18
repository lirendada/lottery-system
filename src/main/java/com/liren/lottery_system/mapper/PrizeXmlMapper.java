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

    /**
     * 根据传入的奖品id获取对应库中的奖品id，用于判断奖品是否存在
     */
    List<Long> listPrizeIdByIds(List<Long> prizeIds);

    /**
     * 根据传入的奖品id，获取对应奖品信息
     */
    List<PrizeEntity> listPrizeByIds(List<Long> prizeIds);
}
