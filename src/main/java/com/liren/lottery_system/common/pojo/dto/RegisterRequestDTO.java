package com.liren.lottery_system.common.pojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "名称不能为空！")
    private String name;

    @NotBlank(message = "邮箱不能为空！")
    private String mail;

    @NotBlank(message = "手机号不能为空！")
    private String phoneNumber;

//    @JsonIgnore
    private String password; // 普通用户不需要密码

    @NotBlank(message = "身份不能为空！")
    private String identity;
}
