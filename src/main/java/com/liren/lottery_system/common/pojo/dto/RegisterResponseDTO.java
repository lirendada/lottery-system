package com.liren.lottery_system.common.pojo.dto;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private long userId;
}
