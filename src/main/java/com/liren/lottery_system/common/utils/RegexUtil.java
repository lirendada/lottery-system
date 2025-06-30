package com.liren.lottery_system.common.utils;

import org.springframework.util.StringUtils;
import java.util.regex.Pattern;

public class RegexUtil {

    /**
     * 邮箱：xxx@xx.xxx（举例：abc@qq.com）
     */
    public static boolean checkMail(String content) {
        if (!StringUtils.hasText(content)) {
            return false;
        }
        String regex = "^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+\\.){1,63}[a-z0-9]+$";
        return Pattern.matches(regex, content);
    }

    /**
     * 手机号码以1开头的11位数字
     */
    public static boolean checkMobile(String content) {
        if (!StringUtils.hasText(content)) {
            return false;
        }
        String regex = "^1[3-9][0-9]{9}$";
        return Pattern.matches(regex, content);
    }

    /**
     * 密码强度正则，6到20位
     */
    public static boolean checkPassword(String content){
        if (!StringUtils.hasText(content)) {
            return false;
        }
        String regex= "^[0-9A-Za-z]{6,20}$";
        return Pattern.matches(regex, content);
    }
}
