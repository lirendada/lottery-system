package com.liren.lottery_system.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum UserStatusEnum {
    AVAILABLE(1, "可使用"),
    DONE(2, "已被抽取");

    private Integer code;
    private String mes;

    public static PrizeStatusEnum forName(String name) {
        for(PrizeStatusEnum e : PrizeStatusEnum.values()) {
            if(e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
