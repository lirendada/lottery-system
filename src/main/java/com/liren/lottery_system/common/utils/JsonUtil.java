package com.liren.lottery_system.common.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.JsonParseException;

import java.util.List;
import java.util.concurrent.Callable;

public class JsonUtil {
    // 单例模式
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil() {}

    private static ObjectMapper getInstance() {
        return objectMapper;
    }


    // 序列化
    public static String toJson(Object object) {
        return JsonUtil.tryParse(() -> {
            return JsonUtil.getInstance().writeValueAsString(object);
        });
    }

    // 反序列化
    public static <T> T toObject(String jsonStr, Class<T> valueType) {
        return JsonUtil.tryParse(() -> {
            return JsonUtil.getInstance().readValue(jsonStr, valueType);
        });
    }

    // 反序列化List
    public static <T> T toList(String jsonStr, Class<?> valueType) {
        // 因为 Jackson 无法通过 Class<List> + Class<User> 推断出 List<User>，
        // 需要显式告诉它泛型参数类型，否则会报错或反序列化成 List<LinkedHashMap>
        JavaType javaType = JsonUtil.getInstance().getTypeFactory()
                .constructParametricType(List.class, valueType);

        return JsonUtil.tryParse(() -> {
            return JsonUtil.getInstance().readValue(jsonStr, javaType);
        });
    }


    // 把Jackson异常统一转成JsonParseException
    private static <T> T tryParse(Callable<T> parser) {
        return tryParse(parser, JacksonException.class);
    }

    private static <T> T tryParse(Callable<T> parser, Class<? extends Exception> check) {
        try {
            return parser.call(); // 执行解析动作
        } catch (Exception e) {
            if (check.isAssignableFrom(e.getClass())) { // 判断e是不是check类型或它的子类
                throw new JsonParseException(e); // 特定异常转换
            }
            throw new IllegalStateException(e); // 其他异常通用处理
        }
    }
}