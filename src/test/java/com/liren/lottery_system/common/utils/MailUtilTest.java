package com.liren.lottery_system.common.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailUtilTest {
    @Autowired
    private MailUtil mailUtil;

    @Test
    void sendSampleMail() {
        mailUtil.sendSampleMail("2916776007@qq.com", "测试", "测试成功！");
    }
}