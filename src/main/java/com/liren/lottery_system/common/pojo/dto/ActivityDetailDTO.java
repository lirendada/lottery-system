package com.liren.lottery_system.common.pojo.dto;

import com.liren.lottery_system.common.enums.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ActivityDetailDTO implements Serializable {
    // 活动信息
    private Long activityId;
    private String activityName;
    private String description;
    private ActivityStatusEnum status;

    public Boolean isValid() {
        if (status.getCode().equals(ActivityStatusEnum.RUNNING.getCode())) {
            return true;
        } else {
            return false;
        }
    }

    // 奖品信息（列表）
    private List<PrizeDTO> prizeDTOList;

    // 人员信息（列表）
    private List<UserDTO> userDTOList;

    @Data
    public static class PrizeDTO {
        private Long prizeId;
        private String name;
        private String description;
        private BigDecimal price;
        private String imageUrl;
        private Long prizeAmount;
        private PrizeTierEnum prizeTiers;
        private PrizeStatusEnum status;

        public Boolean isValid() {
            if (status.getCode().equals(PrizeStatusEnum.AVAILABLE.getCode())) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Data
    public static class UserDTO {
        private Long userId;
        private String userName;
        private UserStatusEnum status;

        public Boolean isValid() {
            if (status.getCode().equals(UserStatusEnum.AVAILABLE.getCode())) {
                return true;
            } else {
                return false;
            }
        }
    }
}
