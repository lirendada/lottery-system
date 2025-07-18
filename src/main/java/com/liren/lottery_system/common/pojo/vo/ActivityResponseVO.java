package com.liren.lottery_system.common.pojo.vo;

import com.liren.lottery_system.common.pojo.ActivityRecord;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ActivityResponseVO implements Serializable {
    private Integer total;
    private List<ActivityRecord> records;
}
