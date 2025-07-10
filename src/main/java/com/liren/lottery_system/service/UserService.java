package com.liren.lottery_system.service;

import com.liren.lottery_system.common.pojo.dto.*;

public interface UserService {
    RegisterResponseDTO register(RegisterRequestDTO req);

    LoginResponseDTO loginPassword(LoginPasswordRequestDTO req);

    LoginResponseDTO loginVerificationCode(LoginVerificationCodeRequestDTO req);
}