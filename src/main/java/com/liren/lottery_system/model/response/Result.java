package com.liren.lottery_system.model.response;

import com.liren.lottery_system.common.enums.GlobalStatusEnum;
import com.liren.lottery_system.model.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success(T data) {
        return new Result(GlobalStatusEnum.SUCCESS.getErrorCode().getCode(),
                null,
                data);
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        Assert.isTrue(!GlobalStatusEnum.SUCCESS.getErrorCode().getCode().equals(code),
                "code 不是错误的异常");
        return new Result(code,
                msg,
                null);
    }

    public static <T> Result<T> fail(ErrorCode errorCode) {
        return fail(errorCode.getCode(), errorCode.getErrMsg());
    }
}
