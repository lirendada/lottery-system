package com.liren.lottery_system.common.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ActivityUserEntity extends BaseEntity {
    private Long activityId;
    private Long userId;
    private String userName;
    private String status;
}
