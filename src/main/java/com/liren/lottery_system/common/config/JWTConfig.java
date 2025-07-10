package com.liren.lottery_system.common.config;

import com.liren.lottery_system.common.utils.JWTUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JWTConfig {
    @Value("${jwt.secret}")
    private String secret; // 不能为static，否则注入不成功，直接为null

    //该方法在注入secret后才执行
    @PostConstruct
    public void init() {
        log.info("【JWTUtils】PostConstruct 正在执行...");
        JWTUtil.init(secret); // 调用工具类的初始化方法
    }
}