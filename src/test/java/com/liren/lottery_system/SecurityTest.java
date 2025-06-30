package com.liren.lottery_system;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.liren.lottery_system.common.constant.Constants;
import com.liren.lottery_system.common.utils.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@SpringBootTest
public class SecurityTest {
    @Test
    public void aes_test() {
        // 获取密钥
        String key = UUID.randomUUID().toString().replace("-", "");
        System.out.println(key);

        // aes加密
        AES aes = SecureUtil.aes(key.getBytes(StandardCharsets.UTF_8));
        String encrypt = aes.encryptHex("123456");
        System.out.println("aes加密后内容：" + encrypt);

        // aes解密
        String decrypt = SecureUtil.aes(key.getBytes(StandardCharsets.UTF_8)).decryptStr(encrypt);
        System.out.println("aes解密后内容：" + decrypt);
    }

    @Test
    public void sha256_test() {
        // 获取盐值
        String salt = UUID.randomUUID().toString().replace("-", "");
        System.out.println("盐值：" + salt);

        String password = "123456";

        // 进行“盐值 + 密码”的sha256转化，然后前面再加上盐值，变成新的密码
        String ret = SecureUtil.sha256(salt + password);
        String newPassword = salt + ret;
        System.out.println("存到数据库中的密码：" + newPassword);

        // 进行校验
        String password1 = "123456";
        String salt1 = newPassword.substring(0, 32);
        String ret1 = SecureUtil.sha256(salt1 + password1);
        System.out.println(ret1);
        if((salt1 + ret1).equals(newPassword)) {
            System.out.println("检验成功！");
        } else {
            System.out.println("校验失败！");
        }
    }

    @Test
    public void testUtil() {
        // 测试对称加密
        String phone = "1231351";
        String encrypt = SecurityUtil.aesEncrypt(phone);
        System.out.println("加密后的手机：" + encrypt);
        String decrypt = SecurityUtil.aesDecrypt(encrypt);
        System.out.println("解密后的手机：" + decrypt);

        // 测试摘要加密
        String passwd = "123123";
        String newPasswd = SecurityUtil.sha256Encrypt(passwd);
        System.out.println("加密后的密码：" + newPasswd);
        System.out.println(SecurityUtil.isValidated(passwd + '1', newPasswd));

        System.out.println(Constants.SYMMETRIC_KEY);
    }
}
