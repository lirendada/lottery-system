package com.liren.lottery_system.common.interceptor;

import com.liren.lottery_system.common.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("user_token");
        log.info("获取路径:{}，从header中获取token:{}", request.getRequestURI(), token);

        Claims claims = JWTUtil.parseJWT(token);
        if(claims == null) {
//            log.info("claims == null");
            response.setStatus(401);
            return false;
        }
        log.info("令牌验证通过, 放行");
        return true;
    }
}
