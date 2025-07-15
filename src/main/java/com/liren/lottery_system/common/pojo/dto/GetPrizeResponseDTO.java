package com.liren.lottery_system.common.pojo.dto;

import com.liren.lottery_system.common.pojo.Record;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPrizeResponseDTO implements Serializable {
    private Integer total;    // 总页数
    private List<Record> records; // 奖品数组
}
