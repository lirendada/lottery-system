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

    public static UserStatusEnum forName(String name) {
        for(UserStatusEnum e : UserStatusEnum.values()) {
            if(e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
