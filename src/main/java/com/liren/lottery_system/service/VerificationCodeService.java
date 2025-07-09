package com.liren.lottery_system.service;

public interface VerificationCodeService {
    String sendVerificationCode(String phoneNumber);

    String getVerificationCode(String phoneNumber);
}
