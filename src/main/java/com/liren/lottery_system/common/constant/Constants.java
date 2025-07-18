package com.liren.lottery_system.common.constant;

import java.util.UUID;

public class Constants {
    // aes对称密钥
    public static final String SYMMETRIC_KEY = "6f2f7405aaeb41aeabef665b1aefb59a";

    // 验证码过期时间
    public static final long CAPTCHA_EXPIRE_TIME = 60L;

    // 手机验证码前缀标识
    public static final String VERIFICATION_CODE_PREFIX = "VERIFICATION_CODE_";

    // TOKEN过期时间
    public static final long TOKEN_EXPIRE_TIME = 3600_000;

    // 活动前缀标识
    public static final String ACTIVITY_PREFIX = "ACTIVITY_";

    // 活动过期时间（默认七天）
    public static final long ACTIVITY_EXPIRE_TIME = 60*60*24*7L;
}
