package com.liren.lottery_system.service.impl;

import com.liren.lottery_system.service.VerificationCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VerificationCodeServiceImplTest {
    @Autowired
    private VerificationCodeService verificationCodeService;

    @Test
    void sendVerificationCode() {
        System.out.println(verificationCodeService.sendVerificationCode("15815307247"));
    }

    @Test
    void getVerificationCode() {
        System.out.println(verificationCodeService.getVerificationCode("15815307247"));
    }
}