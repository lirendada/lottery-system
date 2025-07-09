package com.liren.lottery_system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() {
        stringRedisTemplate.opsForValue().set("name", "liren");
        System.out.println(stringRedisTemplate.opsForValue().get("name"));

        System.out.println(stringRedisTemplate.delete("name"));
    }
}
