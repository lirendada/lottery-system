package com.liren.lottery_system.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer userId;
}
