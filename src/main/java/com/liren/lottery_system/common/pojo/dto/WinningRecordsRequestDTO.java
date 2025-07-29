package com.liren.lottery_system.common.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class WinningRecordsRequestDTO implements Serializable {
    @NotNull(message = "活动id不能为空！")
    private Long activityId;
}
