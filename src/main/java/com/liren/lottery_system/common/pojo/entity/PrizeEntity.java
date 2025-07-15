package com.liren.lottery_system.common.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PrizeEntity extends BaseEntity {
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
}
