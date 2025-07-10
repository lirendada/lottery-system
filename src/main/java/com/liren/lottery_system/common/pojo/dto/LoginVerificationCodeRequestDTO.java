package com.liren.lottery_system.common.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVerificationCodeRequestDTO {
    @NotBlank(message = "手机不能为空")
    private String loginMobile;

    @NotBlank(message = "验证码不能为空")
    private String verificationCode;

    private String mandatoryIdentity;
}
