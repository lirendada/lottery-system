package com.liren.lottery_system.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetActivityRequestDTO implements Serializable {
    private Integer currentPage;
    private Integer pageSize;
}
