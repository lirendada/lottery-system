package com.liren.lottery_system.common.pojo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateActivityRequestDTO implements Serializable {
    @NotBlank(message = "活动名称不能为空！")
    private String activityName;

    @NotBlank(message = "活动描述不能为空！")
    private String description;

    @NotEmpty(message = "活动奖品列表不能为空！")
    @Valid
    private List<ActivityPrizeDTO> activityPrizeList;

    @NotEmpty(message = "活动用户列表不能为空！")
    @Valid
    private List<ActivityUserDTO> activityUserList;
}
