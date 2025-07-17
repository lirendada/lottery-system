package com.liren.lottery_system.common.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ActivityPrizeEntity extends BaseEntity {
    private Long activityId;
    private Long prizeId;
    private Long prizeAmount;
    private String prizeTiers;
    private String status;
}
