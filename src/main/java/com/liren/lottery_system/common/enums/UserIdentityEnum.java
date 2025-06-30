package com.liren.lottery_system.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum UserIdentityEnum {
    ADMIN("管理员"),
    NORMAL("普通用户");

    private String identity;

    public static boolean isValidated(String identity) {
        for(UserIdentityEnum userIdentityEnum : UserIdentityEnum.values()) {
            if(identity.equals(userIdentityEnum.name())) {
                return true;
            }
        }
        return false;
    }
}
