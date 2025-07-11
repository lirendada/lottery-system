package com.liren.lottery_system.service;

import com.liren.lottery_system.common.pojo.dto.*;
import com.liren.lottery_system.common.pojo.entity.UserEntity;
import com.liren.lottery_system.common.pojo.vo.UserResponseVO;

import java.util.List;

public interface UserService {
    RegisterResponseDTO register(RegisterRequestDTO req);

    LoginResponseDTO loginPassword(LoginPasswordRequestDTO req);

    LoginResponseDTO loginVerificationCode(LoginVerificationCodeRequestDTO req);

    List<UserEntity> getUserList();
}