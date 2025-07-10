package com.liren.lottery_system.common.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginPasswordRequestDTO {
    @NotBlank(message = "邮箱或手机不能为空")
    private String loginName;

    @NotBlank(message = "密码不能为空")
    private String password;

    // 强制某身份才可以登录
    private String mandatoryIdentity;
}