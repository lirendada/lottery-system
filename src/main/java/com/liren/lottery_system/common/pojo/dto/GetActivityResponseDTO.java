package com.liren.lottery_system.common.pojo.dto;

import com.liren.lottery_system.common.pojo.ActivityRecord;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GetActivityResponseDTO implements Serializable {
    private Integer total;
    private List<ActivityRecord> records;
}
