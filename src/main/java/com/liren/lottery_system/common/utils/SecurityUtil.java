package com.liren.lottery_system.common.utils;

import cn.hutool.crypto.SecureUtil;
import com.liren.lottery_system.common.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
public class SecurityUtil {

    // aes加密，用于手机号等需要解密使用的字段
    public static String aesEncrypt(String value) {
        if(Constants.SYMMETRIC_KEY == null) {
            throw new NullPointerException("对称密钥为空，请检查！");
        }

        return SecureUtil.aes(Constants.SYMMETRIC_KEY.getBytes(StandardCharsets.UTF_8))
                .encryptHex(value);
    }

    // aes解密，用于手机号等需要解密使用的字段
    public static String aesDecrypt(String encryptStr) {
        if(Constants.SYMMETRIC_KEY == null) {
            throw new NullPointerException("对称密钥为空，请检查！");
        }

        return SecureUtil.aes(Constants.SYMMETRIC_KEY.getBytes(StandardCharsets.UTF_8))
                .decryptStr(encryptStr);
    }

    // sha256加密：摘要加密，不可逆，适用于密码加密
    public static String sha256Encrypt(String password) {
        // 获取盐值
        String salt = UUID.randomUUID().toString().replace("-", "");

        // 进行“盐值 + 密码”的sha256转化，然后前面再加上盐值，变成新的密码
        String newPassword = SecureUtil.sha256(salt + password);
        return salt + newPassword;
    }

    // 校验摘要算法得到的内容是否相同
    public static boolean isValidated(String inputPasswd, String passwd) {
        if(!StringUtils.hasLength(inputPasswd) || !StringUtils.hasLength(passwd)) {
            return false;
        }
        if (passwd.length() != 96) {
            return false;
        }

        // 先拿到盐值
        String salt = passwd.substring(0, 32);

        // 使用摘要算法，进行对比
        String newPassword = SecureUtil.sha256(salt + inputPasswd);
        return (salt + newPassword).equals(passwd);
    }
}
