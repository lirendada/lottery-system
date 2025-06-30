package com.liren.lottery_system;

import com.liren.lottery_system.common.pojo.entity.Encrypt;
import com.liren.lottery_system.mapper.UserXmlMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserXmlMapper userXmlMapper;

    @Test
    public void testPhoneCheck() {
        System.out.println(userXmlMapper.countPhone(new Encrypt("123123")));
    }
}
