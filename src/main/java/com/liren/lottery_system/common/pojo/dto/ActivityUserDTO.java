package com.liren.lottery_system.common.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityUserDTO implements Serializable {
    @NotNull(message = "用户id不能为空！")
    private Long userId;     // 用户id

    @NotBlank(message = "用户名称不能为空！")
    private String userName; // 用户名
}
