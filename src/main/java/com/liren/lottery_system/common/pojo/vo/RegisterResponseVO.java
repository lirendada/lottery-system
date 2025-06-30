package com.liren.lottery_system.common.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private long userId;
}
