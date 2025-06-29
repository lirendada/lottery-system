package com.liren.lottery_system;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogTest {
    private static final Logger logger = LoggerFactory.getLogger(LogTest.class);

    @Test
    public void test() {
        System.out.println("liren");
        logger.info("liren");
    }
}
