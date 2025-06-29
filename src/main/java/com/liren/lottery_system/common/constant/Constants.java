package com.liren.lottery_system.common.constant;

import java.util.UUID;

public class Constants {
    // aes对称密钥
    public static final String SYMMETRIC_KEY = UUID.randomUUID().toString().replace("-", "");
}
