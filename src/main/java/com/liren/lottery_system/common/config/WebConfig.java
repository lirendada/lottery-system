package com.liren.lottery_system.common.config;

import com.liren.lottery_system.common.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 注入自定义的拦截器对象
    @Autowired
    private LoginInterceptor loginInterceptor;

    private final List<String> excludes = Arrays.asList(
            "/**/*.html",
            "/css/**",
            "/js/**",
            "/pic/**",
            "/*.jpg",
            "/favicon.ico",
            "/**/login",
            "/register",
            "/verification-code/send",
            "/winning-records/show",
            "/.well-known/appspecific/com.chrome.devtools.json"
    );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册自定义拦截器对象
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns(excludes)
                .addPathPatterns("/**");
    }
}