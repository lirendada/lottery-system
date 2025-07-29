package com.liren.lottery_system.common.pojo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class DrawPrizeRequestDTO implements Serializable {
    @NotNull(message = "活动ID不能为空！")
    private Long activityId;
    @NotNull(message = "奖品ID不能为空！")
    private Long prizeId;
    @NotNull(message = "中奖时间不能为空！")
    private Date winningTime;

    @NotEmpty(message = "获胜人员列表不能为空！")
    @Valid
    private List<Winner> winnerList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Winner {
        @NotNull(message = "用户ID不能为空！")
        private Long userId;
        @NotBlank(message = "用户名称不能为空！")
        private String userName;
    }
}
