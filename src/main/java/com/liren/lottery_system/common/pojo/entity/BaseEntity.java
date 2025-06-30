package com.liren.lottery_system.common.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {
    private long id;
    private Date gmtCreate;
    private Date gmtModified;
}
