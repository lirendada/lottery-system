package com.liren.lottery_system.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

@Slf4j
@Component
public class RedisUtil {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 插入键值对
     */
    public boolean set(String key, String value) {
        try {
            log.info("插入redis键值对（{}, {}）", key, value);
            stringRedisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("插入redis键值对（{}, {}）失败，{}", key, value, e);
            return false;
        }
    }

    /**
     * 插入包含过期时间的键值对
     */
    public boolean set(String key, String value, long timeout) {
        try {
            log.info("插入redis键值对（{}, {}）", key, value);
            stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("插入redis键值对（{}, {}）失败，{}", key, value, e);
            return false;
        }
    }

    /**
     * 获取键值对
     */
    public Object get(String key) {
        log.info("获取键值对: {}", key);
        return key == null ?  null : stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 删除键值对，可以传多个
     */
    public void delete(String... key) {
        if(key != null && key.length > 0) {
            if(key.length == 1) {
                stringRedisTemplate.delete(key[0]);
            } else {
                stringRedisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 判断键值对是否存在
     */
    public boolean hasKey(String key) {
        try {
            return stringRedisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("判断键值对 {} 是否存在失败, {}", key, e);
            return false;
        }
    }

    /**
     * 获取过期时间
     */
    public long getExpire(String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 设置过期时间
     */
    public boolean setExpire(String key, long timeout) {
        try {
            log.info("键值对 {} 设置过期时间 {} 秒", key, timeout);
            if(timeout > 0) {
                stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("设置键值对 {} 过期时间失败, {}", key, e);
            return false;
        }
    }
}
