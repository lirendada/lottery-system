package com.liren.lottery_system.common.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum PrizeTierEnum {
    FIRST_PRIZE(1, "一等奖"),
    SECOND_PRIZE(2, "二等奖"),
    THIRD_PRIZE(3, "三等奖");

    private Integer code;
    private String mes;

    public static PrizeTierEnum forName(String name) {
        for(PrizeTierEnum e : PrizeTierEnum.values()) {
            if(e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
