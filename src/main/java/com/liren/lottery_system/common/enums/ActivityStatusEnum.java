package com.liren.lottery_system.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ActivityStatusEnum {
    RUNNING(1, "活动执行中"),
    DONE(2, "活动结束");

    private Integer code;
    private String msg;

    public static ActivityStatusEnum forName(String name) {
        for(ActivityStatusEnum e : ActivityStatusEnum.values()) {
            if(e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
