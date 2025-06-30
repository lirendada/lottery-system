package com.liren.lottery_system.service;

import com.liren.lottery_system.common.pojo.dto.RegisterRequestDTO;
import com.liren.lottery_system.common.pojo.dto.RegisterResponseDTO;

public interface UserService {
    RegisterResponseDTO register(RegisterRequestDTO req);
}
