package com.liren.lottery_system.common.pojo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class WinnerRecordEntity extends BaseEntity {
    private Long activityId;
    private String activityName;
    private Long prizeId;
    private String prizeName;
    private String prizeTier;
    private Long winnerId;
    private String winnerName;
    private String winnerEmail;
    private String winnerPhoneNumber;
    private Date winningTime;
}