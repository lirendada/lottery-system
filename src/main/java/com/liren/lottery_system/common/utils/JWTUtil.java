package com.liren.lottery_system.common.utils;


import com.liren.lottery_system.common.constant.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JWTUtil {
    // 没办法直接调用非静态变量secret
    // 所以换个思路，用传参方式来进行初始化
    // 即创建配置类调用init()来进行SECRET_KEY的初始化
    private static Key SECRET_KEY;

    // 由配置类主动调用初始化，对secret进行解码，然后转化为Key类型
    public static void init(String secret) {
        System.out.println("初始化密钥：" + secret);
        SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    /**
     * 根据传入的claims也就是载荷，生成对应的JWT
     */
    public static String createJWT(Map<String, Object> claims) {
        if (SECRET_KEY == null) {
            throw new IllegalStateException("SECRET_KEY 未初始化！");
        }

        String jwt = null;
        try {
            jwt = Jwts.builder()
                    .setClaims(claims)
                    .signWith(SECRET_KEY)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + Constants.TOKEN_EXPIRE_TIME)) // 1小时有效
                    .compact(); // 👈 核心！将 header + payload + signature 拼接、压缩、编码成一个标准的 JWT 字符串。
        } catch (Exception e) {
            log.error("创建令牌出错，错误 = {}", e.getMessage());
        }
        return jwt;
    }

    /**
     * 将生成JWT字符串解析后进行返回
     */
    public static Claims parseJWT(String jwt) {
        if(SECRET_KEY == null || !StringUtils.hasText(jwt)) {
            return null;
        }

        Claims claim = null;
        try {
            claim = (Claims) Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            // ✅ 检查是否过期
            if (claim.getExpiration().before(new Date())) {
                throw new RuntimeException("Token 已过期");
            }
            return claim;

        } catch (ExpiredJwtException e) {
            log.warn("Token 已过期: {}", jwt);
            return null;
        } catch (JwtException e) {
            log.warn("Token 非法: {}", jwt);
            return null;
        } catch (Exception e) {
            log.error("解析令牌出错，token = {}, 错误 = {}", jwt, e.getMessage());
            return null;
        }
    }

    /**
     * 从token中获取用户ID
     */
    public static Integer getUserIdFromToken(String token) {
        Claims claims = JWTUtil.parseJWT(token);
        if(claims != null) {
            return (Integer)claims.get("userId");
        } else {
            return null;
        }
    }
}

