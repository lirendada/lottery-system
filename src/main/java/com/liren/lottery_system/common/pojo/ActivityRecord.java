package com.liren.lottery_system.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRecord implements Serializable {
    private Long activityId;
    private String activityName;
    private String description;
    private Boolean valid;
}
