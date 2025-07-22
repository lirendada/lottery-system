package com.liren.lottery_system.common.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ActivityDetailResponseVO implements Serializable {
    private Long activityId;
    private String activityName;
    private String description;
    private Boolean valid; // 是否有效

    private List<Prize> prizes;

    private List<User> users;

    @Data
    public static class Prize {
        private Long prizeId;
        private String name;
        private String description;
        private BigDecimal price;
        private String imageUrl;
        private Long prizeAmount;
        private String prizeTierName;
        private Boolean valid;
    }

    @Data
    public static class User {
        private Long userId;
        private String userName;
        private Boolean valid;
    }
}
