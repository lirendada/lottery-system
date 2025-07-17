package com.liren.lottery_system.common.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityPrizeDTO implements Serializable {
    @NotNull(message = "奖品id不能为空！")
    private Long prizeId;

    @NotNull(message = "奖品数量不能为空！")
    private Long prizeAmount;

    @NotBlank(message = "奖品级别不能为空！")
    private String prizeTiers;
}
