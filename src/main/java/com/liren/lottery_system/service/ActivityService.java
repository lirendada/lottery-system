package com.liren.lottery_system.service;

import com.liren.lottery_system.common.pojo.dto.CreateActivityRequestDTO;

public interface ActivityService {
    Long createActivity(CreateActivityRequestDTO req);
}
