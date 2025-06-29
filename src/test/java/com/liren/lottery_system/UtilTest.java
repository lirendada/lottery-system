package com.liren.lottery_system;

import com.liren.lottery_system.common.utils.JsonUtil;
import com.liren.lottery_system.model.response.Result;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class UtilTest {
    @Test
    public void jsonTest() {
        Result<String> result = Result.success("liren");

        String str = null;

        // 序列化
        str = JsonUtil.toJson(result);
        System.out.println(str);

        // 反序列化
        Result<String> object = JsonUtil.toObject(str, Result.class);
        System.out.println(object);

        // 序列化list
        List<Result<String>> results = Arrays.asList(
                Result.success("liren"),
                Result.success("yt")
        );
        str = JsonUtil.toJson(results);
        System.out.println(str);

        // 反序列化list
        List<Result<String>> list = JsonUtil.toList(str, Result.class);
        System.out.println(list);
    }
}
