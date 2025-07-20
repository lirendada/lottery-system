package com.liren.lottery_system;

import com.liren.lottery_system.common.pojo.dto.ActivityDetailDTO;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class optionalTest {
    @Test
    public void test() {
        ActivityDetailDTO dto = new ActivityDetailDTO();
        Optional<ActivityDetailDTO> op = Optional.ofNullable(dto);

        Optional<String> optionalName = Optional.of("lirendada");

        // 1. 是否有值
        System.out.println(optionalName.isPresent());

        // 2. 如果有值就打印
        optionalName.ifPresent(name -> {
            System.out.println("name: " + name);
        });

        // 3. 获取值
        String s = optionalName.get();
        System.out.println(s);

        // 4. 如果为空则返回默认值
        String value = (String)Optional.ofNullable(null).orElse("hha ");
        System.out.println(value);

        // 5. 用orElseGet延迟执行
        String value2 = (String)Optional.ofNullable(null).orElseGet(() -> getDefault());
        System.out.println(value2);
        String value3 = (String)Optional.ofNullable("value3").orElse(getDefault());
        System.out.println(value3);

        // 6. 为空抛异常
//        String value4 = (String)Optional.ofNullable(null).orElseThrow();
//        System.out.println(value4);

//        String value5 = (String)Optional.ofNullable(null).orElseThrow(() -> new IllegalArgumentException("不能为空！"));
//        System.out.println(value5);

        // 7. 映射
        String value6 = optionalName.map(name -> {
            name += "hha";
            return name;
        }).orElse("liren");
        System.out.println(value6);

        Optional<Integer> nameLength = optionalName.map(String::length);
        System.out.println(nameLength.get());

        // 8. 链式调用 + 判断
        String value7 = Optional.of("liren")
                .filter(e -> e.length() > 5)
                .map(String::toUpperCase)
                .orElse("太短了！");
        System.out.println(value7);
    }

    public static String getDefault() {
        System.out.println("调用了 getDefault 方法");
        return "默认值";
    }
}
