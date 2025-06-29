package com.liren.lottery_system.model.response;

import com.liren.lottery_system.common.status.GlobalStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success(T data) {
        return new Result(GlobalStatusEnum.SUCCESS.getCode(),
                null,
                data);
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        Assert.isTrue(!GlobalStatusEnum.SUCCESS.getCode().equals(code),
                "code 不是错误的异常");
        return new Result(code,
                msg,
                null);
    }
}
