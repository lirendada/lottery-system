package com.liren.lottery_system.common.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WinningRecordsResponseVO implements Serializable {
    private Long winnerId;
    private String winnerName;
    private String prizeName;
    private String prizeTier;
    private Date winningTime;
}
