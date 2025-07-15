package com.liren.lottery_system.common.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePrizeRequestDTO implements Serializable {
    @NotBlank(message = "奖品名不能为空！")
    private String prizeName;

    private String description;

    @NotNull(message = "奖品价格不能为空！")
    private BigDecimal price;
}
