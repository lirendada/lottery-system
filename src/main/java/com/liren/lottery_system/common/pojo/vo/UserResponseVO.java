package com.liren.lottery_system.common.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseVO implements Serializable {
    private Long userId;
    private String userName;
    private String identity;
}
