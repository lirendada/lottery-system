package com.liren.lottery_system.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrizeRecord implements Serializable {
    private Long prizeId;
    private String prizeName;
    private String description;
    private BigDecimal price;
    private String imageUrl;
}
