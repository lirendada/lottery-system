package com.liren.lottery_system.common.pojo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) // 要打开，不然使用这两个方法的时候会报错
public class UserEntity extends BaseEntity {
    private String userName;
    private String email;
    private Encrypt phoneNumber;
    private String password;
    private String identity;
}
